package ru.practicum.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.Create;
import ru.practicum.Update;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/items")
@Slf4j
public class ItemController {

    private final ItemService itemService;

    @Autowired
    public ItemController (ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public List<ItemDto> getAllItemsByUser(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем все вещи пользователя с id {}", userId);
        return itemService.getAllItemsByUser(userId);
    }

    @GetMapping("/{itemId}")
    public ItemDto getItem(@RequestHeader ("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        log.info("Получаем вещь с id {}", itemId);
        return itemService.getItem(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestHeader ("X-Sharer-User-Id") long userId, @RequestParam String text) {
        log.info("Ищем вещь по описанию", text);
        return itemService.searchItem(userId, text);
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                     @RequestBody ItemDto itemDto) {
        log.info("Добавляем новую вещь пользователю с id {}", userId);
        return itemService.addNewItem(userId, itemDto);
    }


    @PatchMapping("/{itemId}")
    public ItemDto updateItem (@RequestHeader("X-Sharer-User-Id") long userId,
                       @RequestBody ItemDto item, @PathVariable long itemId) {
        log.info("Обновляем вещь с id {}", itemId);
        return itemService.updateItem(item, itemId, userId);
    }
}
