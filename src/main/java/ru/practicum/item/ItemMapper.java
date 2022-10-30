package ru.practicum.item;

import ru.practicum.booking.Booking;
import ru.practicum.user.User;

import java.util.List;

public class ItemMapper {
    public static Item toItem(ItemDto dto, User user) {
        return new Item(dto.getName(), dto.getDescription(), dto.getAvailable(), user);
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
                                            Booking nextBooking, List<CommentDto> comments) {
        ItemInfoDto itemInfo = new ItemInfoDto(item.getId(), item.getName(), item.getDescription(), item.isAvailable(), comments);
        if (lastBooking != null) {
            itemInfo.setLastBooking(new ItemInfoDto.BookingDto(lastBooking.getId(),
                    lastBooking.getBooker().getId()));
        }
        if (nextBooking != null) {
            itemInfo.setNextBooking(new ItemInfoDto.BookingDto(nextBooking.getId(), nextBooking.getBooker().getId()));
        }
        return itemInfo;
    }
}
