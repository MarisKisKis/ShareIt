package ru.practicum.shareit.booking;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exeption.ObjectNotFoundException;
import ru.practicum.shareit.exeption.ValidationException;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
public class BookingServiceImpl implements BookingService {


    private final ItemRepository itemRepository;


    private final UserRepository userRepository;


    private final BookingRepository bookingRepository;

    @Autowired
    public BookingServiceImpl(ItemRepository itemRepository, UserRepository userRepository, BookingRepository bookingRepository) {
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
        this.bookingRepository = bookingRepository;
    }


    @Transactional
    @Override
    public BookingDto addNewBooking(long userId, BookingDtoInput bookingDtoInput) {
        Optional <Item> itemCheck = itemRepository.findById(bookingDtoInput.getItemId());
        if (itemCheck.isEmpty()) {
            throw new ObjectNotFoundException("Вещи не существует");
        }
        if (itemCheck.get().isAvailable() == false) {
            throw new ValidationException("Вещь не доступна для бронирования");
        }
        Optional<User> userCheck = userRepository.findById(userId);
        if (userCheck.isEmpty()) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        if (bookingDtoInput.getEnd().isBefore(bookingDtoInput.getStart())) {
            throw new ValidationException("Окончание бронирования не может раньше начала");
        }
        if (bookingDtoInput.getStart().isAfter(bookingDtoInput.getEnd())) {
            throw new ValidationException(("Время начала бронирования не может быть после времени окончания"));
        }
        if (bookingDtoInput.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Время начала бронирования не может быть в прошлом");
        }
        if (userId == itemCheck.get().getUser().getId()) {
            throw new ObjectNotFoundException("Владелец вещи не может сам у себя ее забронировать");
        }
        Item item = itemCheck.get();
        User user = userCheck.get();
        List<Booking> bookings = bookingRepository.findByItemIdOrderByStartDesc(item.getId());
        for (Booking booking : bookings) {
            if (bookingDtoInput.getStart().isBefore(booking.getEnd())) {
                throw new ValidationException("Вещь недоступна");
            }
        }
        Booking booking = BookingMapper.toBooking(bookingDtoInput, user, item, BookingStatus.WAITING);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Transactional
    @Override
    public BookingDto approveBooking(long userId, long id, String approved) {
        Optional <Booking> optBooking = bookingRepository.findById(id);
        Booking booking = optBooking.get();
        if (optBooking.isEmpty()) {
            throw new ObjectNotFoundException("Бронирования нет в базе");
        }
        Optional<Item> itemOpt = itemRepository.findById(booking.getItem().getId());
        Item item = itemOpt.get();
        if (userId != item.getUser().getId()) {
            throw new ObjectNotFoundException("У Вещи другой владелец");
        }
        if (approved.equals("true")) {
            if (booking.getStatus().equals(BookingStatus.APPROVED)) {
                throw new ValidationException("Бронирование уже было подтверждено");
            }
            booking.setStatus(BookingStatus.APPROVED);
        }
        if (approved.equals("false")) {
            if (booking.getStatus().equals(BookingStatus.REJECTED)) {
                throw new ValidationException("Бронирование уже было отклонено");
            }
            booking.setStatus(BookingStatus.REJECTED);
        }
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Transactional
    @Override
    public BookingDto findBookingById(long userId, long id) {
        Optional <Booking> booking = bookingRepository.findById(id);
        if (booking.isEmpty()) {
            throw new ObjectNotFoundException("Нет бронирования с таким id");
        }
        Optional<Item> item = itemRepository.findById(booking.get().getItem().getId());
        if (item.isEmpty()) {
            throw new ObjectNotFoundException("Нет такого пользователя");
        }
        if ((booking.get().getBooker().getId() != userId) && (userId != item.get().getUser().getId())) {
            throw new ObjectNotFoundException("Бронирование с этим id не принадлежит этому пользователю и пользователь" +
                    " не является владельцем");
        }
        return BookingMapper.toBookingDto(booking.get());
    }

    @Transactional
    @Override
    public List<BookingDto> getUserBookings(long userId, BookingState state, Integer from, Integer size) {
        validate(userId, from, size);
        Optional<User> user = userRepository.findById(userId);
        if ((user == null)|| (user.isEmpty())) {
            throw new ObjectNotFoundException("Нет пользователя c таким id");
        }
        if (userId == 0) {
            throw new ObjectNotFoundException("Отсутствует id пользователя");
        }
        Sort startSortDesc = Sort.by(Sort.Direction.DESC, "start");
        Pageable pageable = PageRequest.of(from / size, size, startSortDesc);
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                 bookings = bookingRepository.findAllByBookerId(userId, pageable);
                 break;
            case FUTURE:
                 bookings = bookingRepository.findByBookerIdAndStartIsAfter(userId, LocalDateTime.now(), pageable);
                break;
            case CURRENT:
                bookings = bookingRepository.findByBookerIdAndEndIsAfterAndStartIsBefore(userId,
                                LocalDateTime.now(), LocalDateTime.now(), pageable);
                break;
            case PAST:
                bookings = bookingRepository.findByBookerIdAndEndIsBefore(userId, LocalDateTime.now(),
                        pageable);
                break;
            case WAITING:
                return bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.WAITING, pageable)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(toList());
            case REJECTED:
                bookings = bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.REJECTED, pageable);
            break;
        }
        return bookings
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<BookingDto> getAllItemsBookings(long ownerId, BookingState state, Integer from, Integer size) {
        validate(ownerId, from, size);
        Sort startSortDesc = Sort.by(Sort.Direction.DESC, "start");
        Pageable pageable = PageRequest.of(from / size, size, startSortDesc);
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings = bookingRepository.findByItemId_UserId(ownerId, pageable);
                break;
            case FUTURE:
                bookings = bookingRepository.findByItemId_UserIdAndStartIsAfter(ownerId, LocalDateTime.now(), pageable);
;               break;
            case CURRENT:
                bookings = bookingRepository.findByItemId_UserIdAndEndIsAfterAndStartIsBefore(ownerId, LocalDateTime.now(),
                                LocalDateTime.now(), pageable);
                break;
            case PAST:
                bookings = bookingRepository.findByItemId_UserIdAndEndIsBefore(ownerId, LocalDateTime.now(), pageable);
                break;
            case WAITING:
                bookings = bookingRepository.findByItemId_User_IdAndStatus(ownerId, BookingStatus.WAITING, pageable);
                break;
            case REJECTED:
                bookings = bookingRepository.findByItemId_User_IdAndStatus(ownerId, BookingStatus.REJECTED, pageable);
                break;
        }
        return bookings
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    private void validate(Long ownerId, Integer from, Integer size) {
        Optional<User> user = userRepository.findById(ownerId);
        if (user.isEmpty()) {
            throw new ObjectNotFoundException("Нет такого пользователя");
        }
        if (from < 0) {
            throw new ValidationException("from меньше 0");
        }
        if (size <= 0) {
            throw new ValidationException("size меньше либо равно 0");
        }
    }
}
