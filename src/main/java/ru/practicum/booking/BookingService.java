package ru.practicum.booking;

import java.util.List;

public interface BookingService {
    BookingDto addNewBooking(long userId, BookingDtoInput bookingDtoInput);

    BookingDto approveBooking(long userId, long bookingId, String approved);

    BookingDto findBookingById(long userId, long bookingId);

    List<BookingDto> getUserBookings(long userId, BookingState state);

    List<BookingDto> getAllItemsBookings(long ownerId, BookingState state);
}
