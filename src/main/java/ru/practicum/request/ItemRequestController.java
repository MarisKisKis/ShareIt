package ru.practicum.request;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {

    private final ItemRequestService itemRequestService;

    @Autowired
    public ItemRequestController(ItemRequestService itemRequestService) {
        this.itemRequestService = itemRequestService;
    }

    @GetMapping
    public List<RequestAndResponseDto> findOwnerRequests(@RequestHeader("X-Sharer-User-Id") long userId) {
        log.info("Получаем запросы обладателя");
        return itemRequestService.findOwnerRequests(userId);
    }

    @GetMapping("/all")
    public List<RequestAndResponseDto> findUserRequests(@RequestHeader("X-Sharer-User-Id") long userId,
                                                             @RequestParam (name = "from", required = false) Integer from,
                                                             @RequestParam (name = "size", required = false) Integer size) {
        log.info("Получаем запросы пользователя");
        return itemRequestService.findUserRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public RequestAndResponseDto getRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                                @PathVariable(name = "requestId") long requestId) {
        log.info("Получен запрос с id {}", requestId);
        return itemRequestService.getRequest(userId, requestId);
    }

    @PostMapping
    public ItemRequestDto createRequest(@RequestHeader("X-Sharer-User-Id") long userId,
                                              @Validated @RequestBody ItemRequestDto requestDto) {
        log.info("Создаем запрос");
        return itemRequestService.createRequest(userId, requestDto);
    }

}
