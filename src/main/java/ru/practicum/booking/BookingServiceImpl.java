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
import java.util.List;
import java.util.Optional;

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
    public BookingDto addNewBooking(long userId, BookingDto bookingDto) {
        Optional <Item> itemCheck = itemRepository.findById(bookingDto.getItem().getId());
        /*
        if (itemOpt.isEmpty()) {
            throw new ObjectNotFoundException("Item c id = {} не существует", bookingDtoInput.getItemId());
        }


        if (item.get().equals(false)) {
            throw new ValidationException("Item не доступна для бронирования");
        }

         */
        Optional<User> userCheck = userRepository.findById(userId);
        if (userCheck.isEmpty()) {
            throw new ObjectNotFoundException("Пользователь не найден");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new ValidationException("Окончание бронирования не может раньше начала");
        }
        if (bookingDto.getStart().isAfter(bookingDto.getEnd())) {
            throw new ValidationException(("Время начала бронирования не может быть после времени окончания"));
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Время начала бронирования не может быть в прошлом");
        }
        /*
        if (userId.equals(itemCheck.get().getOwner().getId())) {
            throw new ObjectNotFoundException("Владелец вещи не может сам у себя ее забронировать");
        }

         */
        Item item = itemCheck.get();
        User user = userCheck.get();
        List<Booking> bookings = bookingRepository.findByItemIdOrderByStartDesc(item.getId());
        for (Booking booking : bookings) {
            if (bookingDto.getStart().isBefore(booking.getEnd())) {
                throw new ValidationException("Вещь не доступна к бронированию в это время");
            }
        }
        Booking booking = BookingMapper.toBooking(bookingDto, user, item, BookingStatus.WAITING);
        return BookingMapper.toBookingDto(bookingRepository.save(booking));
    }

    @Transactional
    @Override
    public BookingDto approveBooking(Long userId, Long bookingId, String approved) {
        Booking booking = bookingRepository.getById(bookingId);
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
    public BookingDto findBookingById(long userId, Long bookingId) {
        return BookingMapper.toBookingDto(bookingRepository.getById(bookingId));
    }

    @Transactional
    @Override
    public List<BookingDto> getUserBookings(Long userId, BookingState state) {
        Sort startSortDesc = Sort.by(Sort.Direction.DESC, "start");
        switch (state) {
            case ALL:
                return bookingRepository.findAllByBookerId(userId, startSortDesc)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(toList());
            case FUTURE:
                return bookingRepository.findByBookerIdAndStartIsAfter(userId, LocalDateTime.now(),
                                startSortDesc)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(toList());
            case CURRENT:
                return bookingRepository.findByBookerIdAndEndIsAfterAndStartIsBefore(userId,
                                LocalDateTime.now(), LocalDateTime.now(), startSortDesc)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(toList());
            case PAST:
                return bookingRepository.findByBookerIdAndEndIsBefore(userId, LocalDateTime.now(),
                                startSortDesc)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(toList());
            case WAITING:
                return bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.WAITING, startSortDesc)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(toList());
            case REJECTED:
                return bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.REJECTED, startSortDesc)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(toList());
            default:
                throw new ValidationException("UNSUPPORTED_STATUS");
        }
    }

    @Transactional
    @Override
    public List<BookingDto> getAllItemsBookings(long ownerId, BookingState state) {
        Sort startSortDesc = Sort.by(Sort.Direction.DESC, "start");
        switch (state) {
            case ALL:
                return bookingRepository.findByItemId_UserId(ownerId, startSortDesc)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(toList());
            case FUTURE:
                return bookingRepository.findByItemId_UserIdAndStartIsAfter(ownerId, LocalDateTime.now(), startSortDesc)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(toList());
            case CURRENT:
                return bookingRepository.findByItemId_UserIdAndEndIsAfterAndStartIsBefore(ownerId, LocalDateTime.now(),
                                LocalDateTime.now(), startSortDesc)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(toList());
            case PAST:
                return bookingRepository.findByItemId_UserIdAndEndIsBefore(ownerId, LocalDateTime.now(), startSortDesc)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(toList());
            case WAITING:
                return bookingRepository.findByItemId_User_IdAndStatus(ownerId, BookingStatus.WAITING, startSortDesc)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(toList());
            case REJECTED:
                return bookingRepository.findByItemId_User_IdAndStatus(ownerId, BookingStatus.REJECTED, startSortDesc)
                        .stream()
                        .map(BookingMapper::toBookingDto)
                        .collect(toList());
            default:
                throw new ValidationException("UNSUPPORTED_STATUS");
        }
    }
}
