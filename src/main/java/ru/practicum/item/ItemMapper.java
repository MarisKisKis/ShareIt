package ru.practicum.item;

import ru.practicum.booking.Booking;
import ru.practicum.request.ItemRequest;
import ru.practicum.user.User;

import java.util.List;

public class ItemMapper {
    public static Item toItem(ItemDto dto, User user, ItemRequest request) {
        Item item = Item.builder()
                .name(dto.getName())
                .description(dto.getDescription())
                .available(dto.getAvailable())
                .user(user)
                .build();
        if (request != null) {
            item.setRequest(request);
        }
        return item;
    }

    public static ItemDto toItemDto(Item item) {
        ItemDto itemDto = ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.isAvailable())
                .build();
        if (item.getRequest() != null) {
            itemDto.setRequestId(item.getRequest().getRequestId());
        }
        return itemDto;
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
