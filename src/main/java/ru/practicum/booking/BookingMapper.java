package ru.practicum.booking;


import ru.practicum.item.Item;
import ru.practicum.user.User;

public class BookingMapper {
    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getBookingId(),
                booking.getStart(),
                booking.getEnd(),
                new BookingDto.ItemDto(booking.getItem().getId(),
                        booking.getItem().getName(),
                        booking.getItem().getDescription(),
                        booking.getItem().isAvailable()),
                new BookingDto.UserDto(booking.getBooker().getId(),
                        booking.getBooker().getName(),
                        booking.getBooker().getEmail()),
                booking.getStatus()
        );
    }


    public static Booking toBooking(BookingDtoInput bookingDto, User user, Item item, BookingStatus status) {
        return new Booking(bookingDto.getId(), bookingDto.getStart(), bookingDto.getEnd(), item, user, status);
    }
}
