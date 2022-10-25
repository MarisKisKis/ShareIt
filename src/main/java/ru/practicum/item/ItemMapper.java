package ru.practicum.item;

import ru.practicum.booking.Booking;
import ru.practicum.user.User;

import java.util.List;

public class ItemMapper {
    public static Item toItem(ItemDto dto, User user) {
        return new Item(dto.getId(), dto.getName(), dto.getDescription(), dto.getAvailable(), user);
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.isAvailable()
        );
    }

    public static ItemInfoDto toItemInfoDto(Item item, Booking lastBooking,
                                            Booking nextBooking, List<CommentDto> commentsDto) {
        return new ItemInfoDto(item.getId(), item.getName(), item.getDescription(), item.isAvailable(),
                new ItemInfoDto.BookingDto(lastBooking.getBookingId(), lastBooking.getStart(), lastBooking.getEnd(),
                        lastBooking.getBooker().getId()),
                new ItemInfoDto.BookingDto(nextBooking.getBookingId(), nextBooking.getStart(), nextBooking.getEnd(),
                        nextBooking.getBooker().getId()), commentsDto);
    }
}
