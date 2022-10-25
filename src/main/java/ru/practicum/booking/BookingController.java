package ru.practicum.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.exeption.ValidationException;
import ru.practicum.item.ItemDto;

import java.util.List;

@RestController
@RequestMapping("/bookings")
@Slf4j
public class BookingController {

    private final BookingService bookingService;

    @Autowired
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/{bookingId}")
    public BookingDto findBookingById(@RequestHeader("X-Sharer-User-Id") long userId,
                                      @PathVariable Long bookingId) {
        log.info("Получаем информацию о бронировании пользователя с id {}", userId);
        return bookingService.findBookingById(userId, bookingId);
    }

    @GetMapping
    public List<BookingDto> getUserBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @RequestParam(name = "state", required = false,
                                                    defaultValue = "ALL") String bookState) {
        log.info("Получаем список всех бронирований текущего пользователя с id {}", userId);
        BookingState state = BookingState.valueOf(bookState);
        return bookingService.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingDto> getAllItemsBookings(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                @RequestParam(name = "state", required = false,
                                                        defaultValue = "ALL") String bookState) {
        log.info("Получаем список бронирований для всех вещей текущего пользователя с id {}", ownerId);
        BookingState state = BookingState.valueOf(bookState);
        return bookingService.getAllItemsBookings(ownerId, state);
    }

    @PostMapping
    public BookingDto addBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                 @RequestBody BookingDto bookingDto) {
        log.info("Добавляем новое бронирование пользователю с id {}", userId);
        return bookingService.addNewBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingDto approveBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @PathVariable long bookingId,
                                     @RequestParam String approved) {
        log.info("Отправляем запрос на бронирование");
        return bookingService.approveBooking(userId, bookingId, approved);
    }
}
