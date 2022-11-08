package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@Controller
@RequestMapping(path = "/items")
@RequiredArgsConstructor
@Slf4j
@Validated
public class ItemController {

    private final ItemClient itemClient;

    @GetMapping
    public ResponseEntity<Object> getAllItemsByUser(@RequestHeader("X-Sharer-User-Id") long userId,
                                                    @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                    @Positive @RequestParam(name = "size", defaultValue = "20") Integer size) {
        log.info("Получаем все вещи пользователя с id {}", userId);
        return itemClient.getAllItemsByUser(userId, from, size);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItem(@RequestHeader ("X-Sharer-User-Id") long userId, @PathVariable long itemId) {
        log.info("Получаем вещь с id {}", itemId);
        return itemClient.getItem(userId, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItem(@RequestHeader ("X-Sharer-User-Id") long userId, @RequestParam String text,
                                             @PositiveOrZero @RequestParam(name = "from", defaultValue = "0") Integer from,
                                             @Positive @RequestParam(name = "size", defaultValue = "20") Integer size) {
        log.info("Ищем вещь по описанию", text);
        return itemClient.searchItem(userId, text,  from, size);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@RequestHeader("X-Sharer-User-Id") long userId,
                           @RequestBody ItemDto itemDto) {
        log.info("Добавляем новую вещь пользователю с id {}", userId);
        return itemClient.addNewItem(userId, itemDto);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") long userId,
                                    @RequestBody CommentDto commentDto,
                                    @PathVariable long itemId) {
        log.info("Добавляем комментарий к вещи с id {}", itemId);
        return itemClient.addComment(userId, commentDto, itemId);
    }


    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem (@RequestHeader("X-Sharer-User-Id") long userId,
                               @RequestBody ItemDto item, @PathVariable long itemId) {
        log.info("Обновляем вещь с id {}", itemId);
        return itemClient.updateItem(item, itemId, userId);
    }
}
