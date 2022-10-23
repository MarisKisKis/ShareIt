package ru.practicum.booking;


public class BookingMapper {
    public static Booking toBooking(BookingDto dto) {
        return new Booking(dto.getBookingId(), dto.getStart(), dto.getEnd(), dto.getItemId(), dto.getBookerId(),
                dto.getStatus());
    }

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(booking.getBookingId(), booking.getStart(), booking.getEnd(), booking.getItemId(), booking.getBookerId(),
                booking.getStatus());
    }
}
