package ru.practicum.item;

import org.springframework.stereotype.Component;
import ru.practicum.user.UserService;

import java.util.*;

@Component
public class ItemRepositoryImpl implements ItemRepository {
    private final Map<Long, ItemDto> items = new HashMap<>();
    private Long id = 1L;

    private final UserService userService;

    public ItemRepositoryImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ItemDto save(ItemDto item) {
        item.setId(id);
        items.put(item.getId(), item);
        id++;
        return item;
    }

    @Override
    public ItemDto findItemById(long itemId) {
        return items.get(itemId);
    }

    @Override
    public List<ItemDto> getAllItemsByUser(long userId) {
        List<ItemDto> allUserItems = new ArrayList<>();
        for (ItemDto item : items.values()) {
            if (item.getUserId() == userId) {
                allUserItems.add(item);
            }
        }
        return allUserItems;
    }

    @Override
    public List<ItemDto> searchItem(long userId, String text) {
        List<ItemDto> targetItems = new ArrayList<>();
        if (text.isEmpty()) {
            return targetItems;
        }
        List<ItemDto> allItems = new ArrayList<>();
        for (ItemDto item : items.values()) {
            allItems.add(item);
        }
        for (ItemDto itemForSearch : allItems) {
            if (itemForSearch.getAvailable() == true) {
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
    public ItemDto updateItem(ItemDto item, long itemId) {
        ItemDto updatedItem = items.get(itemId);
        if (item.getAvailable() != null) {
            updatedItem.setAvailable(item.getAvailable());
        }
        if (item.getName() != null) {
            updatedItem.setName(item.getName());
        }
        if (item.getDescription() != null) {
            updatedItem.setDescription(item.getDescription());
        }
        items.put(itemId, updatedItem);
        return updatedItem;
    }
}
