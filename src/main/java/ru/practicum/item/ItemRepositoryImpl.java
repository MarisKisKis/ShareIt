package ru.practicum.item;

import org.springframework.stereotype.Component;
import ru.practicum.user.UserService;

import java.util.*;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, Item> items = new HashMap<>();
    private Long id = 1L;

    private final UserService userService;

    public ItemRepositoryImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public Item save(Item item) {
        item.setId(id);
        items.put(item.getId(), item);
        id++;
        return item;
    }

    @Override
    public Item findItemById(long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<Item> getAllItemsByUser(long userId) {
        List<Item> allUserItems = new ArrayList<>();
        for (Item item : items.values()) {
            if (item.getUserId() == userId) {
                allUserItems.add(item);
            }
        }
        return allUserItems;
    }

    @Override
    public List<Item> searchItem(long userId, String text) {
        List<Item> targetItems = new ArrayList<>();
        if (text.isEmpty()) {
            return targetItems;
        }
        List<Item> allItems = new ArrayList<>();
        for (Item item : items.values()) {
            allItems.add(item);
        }
        for (Item itemForSearch : allItems) {
            if (itemForSearch.isAvailable() == true) {
                String itemName = itemForSearch.getName().toLowerCase();
                String itemDescription = itemForSearch.getDescription().toLowerCase();
                if (itemName.contains(text.toLowerCase()) || itemDescription.contains(text.toLowerCase())) {
                    targetItems.add(itemForSearch);
                }
            }
        }
        return targetItems;
    }

    @Override
    public Item updateItem(Item item, long itemId) {
        items.put(item.getId(), item);
        return item;
    }
}
