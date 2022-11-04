package ru.practicum.shareit.request;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;


@Controller
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
@Slf4j
@Validated
public class RequestController {

    private final RequestClient requestClient;

    @GetMapping
    public ResponseEntity<Object> findOwnerRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем запросы обладателя");
        return requestClient.findOwnerRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findUserRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                   @PositiveOrZero @RequestParam(name = "from", required = false) Integer from,
                                                   @Positive @RequestParam (name = "size", required = false) Integer size) {
        log.info("Получаем запросы пользователя");
        return requestClient.findUserRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                            @PathVariable(name = "requestId") long requestId) {
        log.info("Получен запрос с id {}", requestId);
        return requestClient.getRequest(userId, requestId);
    }

    @PostMapping
    public ResponseEntity<Object> createRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                        @Validated @RequestBody RequestDto requestDto) {
        log.info("Создаем запрос");
        return requestClient.createRequest(userId, requestDto);
    }
}
