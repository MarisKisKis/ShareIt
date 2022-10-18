package ru.practicum.item;

import java.util.List;

public interface ItemRepository {

    ItemDto findItemById (long itemId);

    List <ItemDto> getAllItemsByUser(long userId);
    ItemDto save(ItemDto item);

    List<ItemDto> searchItem(long userId, String text);

    ItemDto updateItem (ItemDto item, long itemId);
}
