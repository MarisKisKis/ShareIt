package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

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
    public List<ItemInfoDto> getAllItemsByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                               @RequestParam(name = "from", defaultValue = "0") Integer from,
                                               @RequestParam(name = "size", defaultValue = "20") Integer size) {
        log.info("Получаем все вещи пользователя с id {}", userId);
        return itemService.getAllItemsByUser(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ItemInfoDto getItem(@RequestHeader ("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        log.info("Получаем вещь с id {}", itemId);
        return itemService.getItem(userId, itemId);
    }

    @GetMapping("/search")
    public List<ItemDto> searchItem(@RequestHeader ("X-Sharer-User-Id") long userId, @RequestParam String text,
                                    @RequestParam(name = "from", defaultValue = "0") Integer from,
                                    @RequestParam(name = "size", defaultValue = "20") Integer size) {
        log.info("Ищем вещь по описанию", text);
        return itemService.searchItem(userId, text,  from, size);
    }

    @PostMapping
    public ItemDto addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                     @RequestBody ItemDto itemDto) {
        log.info("Добавляем новую вещь пользователю с id {}", userId);
        return itemService.addNewItem(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @RequestBody CommentDto commentDto,
                                    @PathVariable long itemId) {
        log.info("Добавляем комментарий к вещи с id {}", itemId);
        return itemService.addComment(userId, commentDto, itemId);
    }


    @PatchMapping("/{itemId}")
    public ItemDto updateItem (@RequestHeader("X-Sharer-User-Id") long userId,
                       @RequestBody ItemDto item, @PathVariable long itemId) {
        log.info("Обновляем вещь с id {}", itemId);
        return itemService.updateItem(item, itemId, userId);
    }
}
