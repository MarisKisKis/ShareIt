package ru.practicum.item;

public class ItemMapper {
    public static Item toItem(ItemDto dto) {
        return new Item(dto.getId(), dto.getName(), dto.getDescription(), dto.getAvailable(), dto.getUserId(),
                dto.getRequest());
    }

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(item.getId(), item.getName(), item.getDescription(), item.isAvailable(), item.getUserId(),
                item.getRequest());
    }
}
