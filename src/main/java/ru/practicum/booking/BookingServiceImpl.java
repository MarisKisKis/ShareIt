package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.exeption.ObjectNotFoundException;
import ru.practicum.exeption.ValidationException;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

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
    public BookingDto approveBooking(long userId, long bookingId, String approved) {
        Optional <Booking> optBooking = bookingRepository.findById(bookingId);
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
    public BookingDto findBookingById(long userId, long bookingId) {
        Optional <Booking> booking = bookingRepository.findById(bookingId);
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
    public List<BookingDto> getUserBookings(long userId, BookingState state) {
        Optional<User> user = userRepository.findById(userId);
        if ((user == null)|| (user.isEmpty())) {
            throw new ObjectNotFoundException("Нет пользователя c таким id");
        }
        if (userId == 0) {
            throw new ObjectNotFoundException("Отсутствует id пользователя");
        }
        Sort startSortDesc = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                 bookings = bookingRepository.findAllByBookerId(userId, startSortDesc);
                 break;
            case FUTURE:
                 bookings = bookingRepository.findByBookerIdAndStartIsAfter(userId, LocalDateTime.now(),
                                startSortDesc);
                break;
            case CURRENT:
                bookings = bookingRepository.findByBookerIdAndEndIsAfterAndStartIsBefore(userId,
                                LocalDateTime.now(), LocalDateTime.now(), startSortDesc);
                break;
            case PAST:
                bookings = bookingRepository.findByBookerIdAndEndIsBefore(userId, LocalDateTime.now(),
                                startSortDesc);
                break;
            case WAITING:
                return bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.WAITING, startSortDesc)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(toList());
            case REJECTED:
                bookings = bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.REJECTED, startSortDesc);
            break;
        }
        return bookings
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public List<BookingDto> getAllItemsBookings(long ownerId, BookingState state) {
        Optional<User> user = userRepository.findById(ownerId);
        if (user.isEmpty()) {
            throw new ObjectNotFoundException("Нет пользователя с таким id");
        }
        Sort startSortDesc = Sort.by(Sort.Direction.DESC, "start");
        List<Booking> bookings = new ArrayList<>();
        switch (state) {
            case ALL:
                bookings = bookingRepository.findByItemId_UserId(ownerId, startSortDesc);
                break;
            case FUTURE:
                bookings = bookingRepository.findByItemId_UserIdAndStartIsAfter(ownerId, LocalDateTime.now(), startSortDesc);
;               break;
            case CURRENT:
                bookings = bookingRepository.findByItemId_UserIdAndEndIsAfterAndStartIsBefore(ownerId, LocalDateTime.now(),
                                LocalDateTime.now(), startSortDesc);
                break;
            case PAST:
                bookings = bookingRepository.findByItemId_UserIdAndEndIsBefore(ownerId, LocalDateTime.now(), startSortDesc);
                break;
            case WAITING:
                bookings = bookingRepository.findByItemId_User_IdAndStatus(ownerId, BookingStatus.WAITING, startSortDesc);
                break;
            case REJECTED:
                bookings = bookingRepository.findByItemId_User_IdAndStatus(ownerId, BookingStatus.REJECTED, startSortDesc);
                break;
        }
        return bookings
                .stream()
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
