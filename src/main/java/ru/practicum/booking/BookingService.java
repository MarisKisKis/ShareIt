package ru.practicum.booking;

import java.util.List;

public interface BookingService {
    BookingDto addNewBooking(long userId, BookingDtoInput bookingDtoInput);

    BookingDto approveBooking(long userId, long id, String approved);

    BookingDto findBookingById(long userId, long id);

    List<BookingDto> getUserBookings(long userId, BookingState state, Integer from, Integer size);

    List<BookingDto> getAllItemsBookings(long ownerId, BookingState state, Integer from, Integer size);
}
