package ru.practicum.shareit.item;

import java.util.List;


public interface ItemService {
    ItemDto addNewItem (long userId, ItemDto item);

    ItemInfoDto getItem (long userId, long itemId);

    List <ItemInfoDto> getAllItemsByUser(long userId, Integer from, Integer size);

    List<ItemDto> searchItem (long userId, String text, Integer from, Integer size);

    ItemDto updateItem (ItemDto item, long itemId, long userId);

    CommentDto addComment(long userId, CommentDto commentDto, long itemId);
}
