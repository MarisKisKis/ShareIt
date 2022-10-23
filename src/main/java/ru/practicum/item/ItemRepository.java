package ru.practicum.item;

import java.util.List;

public interface ItemRepository {

    Item findItemById (long itemId);

    List <Item> getAllItemsByUser(long userId);

    Item save(Item item);

    List<Item> searchItem(long userId, String text);

    Item updateItem (Item item, long itemId);
}
