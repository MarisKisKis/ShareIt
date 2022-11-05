package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @GetMapping
    public ResponseEntity<Object> getUserBookings(@RequestHeader("X-Sharer-User-Id") long userId,
                                                  @RequestParam(name = "state", required = false,
                                                          defaultValue = "ALL") String bookState,
                                                  @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @Positive @RequestParam(name = "size", defaultValue = "20") Integer size) {
        BookingState state = BookingState.from(bookState)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + bookState));
        log.info("Получаем список всех бронирований текущего пользователя с id {}", userId);
        return bookingClient.getUserBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getAllItemsBookings(@RequestHeader("X-Sharer-User-Id") long ownerId,
                                                      @RequestParam(name = "state", required = false,
                                                              defaultValue = "ALL") String bookState,
                                                      @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                      @Positive @RequestParam(name = "size", defaultValue = "20") Integer size) {
        BookingState state = BookingState.from(bookState)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + bookState));
        log.info("Получаем список бронирований для всех вещей текущего пользователя с id {}", ownerId);
        return bookingClient.getAllItemsBookings(ownerId, state, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader("X-Sharer-User-Id") long userId,
                                           @RequestBody @Valid BookItemRequestDto requestDto) {
        log.info("Creating booking {}, userId={}", requestDto, userId);
        return bookingClient.bookItem(userId, requestDto);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                             @PathVariable Long bookingId) {
        log.info("Get booking {}, userId={}", bookingId, userId);
        return bookingClient.getBooking(userId, bookingId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> approveBooking(@RequestHeader("X-Sharer-User-Id") long userId,
                                     @PathVariable long bookingId,
                                     @RequestParam String approved) {
        log.info("Отправляем запрос на бронирование");
        return bookingClient.approveBooking(userId, bookingId, approved);
    }
}
