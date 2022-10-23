package ru.practicum.booking;

import java.util.List;

public interface BookingService {
    BookingDto addNewBooking(long userId, BookingDto bookingDto);

    BookingDto approveBooking(Long userId, Long bookingId, String approved);

    BookingDto findBookingById(long userId, Long bookingId);

    List<BookingDto> getUserBookings(Long userId, BookingState state);

    List<BookingDto> getAllItemsBookings(long ownerId, BookingState state);
}
