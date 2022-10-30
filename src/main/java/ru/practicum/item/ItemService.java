package ru.practicum.item;

import java.util.List;


public interface ItemService {
    ItemDto addNewItem (long userId, ItemDto item);

    ItemInfoDto getItem (long userId, long itemId);

    List <ItemInfoDto> getAllItemsByUser(long userId);

    List<ItemDto> searchItem (long userId, String text);

    ItemDto updateItem (ItemDto item, long itemId, long userId);

    CommentDto addComment(long userId, CommentDto commentDto, long itemId);
}
