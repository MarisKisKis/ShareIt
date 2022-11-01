package ru.practicum.request;

import ru.practicum.item.Item;
import ru.practicum.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class ItemRequestMapper {

    public static ItemRequestDto toItemRequestDto(ItemRequest itemRequest) {
        return new ItemRequestDto (itemRequest.getRequestId(), itemRequest.getDescription(), itemRequest.getCreated());
    }

    public static ItemRequest toItemRequest(ItemRequestDto requestDto, User user) {
        return new ItemRequest(requestDto.getId(), requestDto.getDescription(), user, LocalDateTime.now());
    }

    public static RequestAndResponseDto toRequestAndResponseDto(ItemRequest request, List<Item> itemList) {
        List<RequestAndResponseDto.ItemDto> itemDtoList = itemList.stream()
                .map(item -> new RequestAndResponseDto.ItemDto(item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.isAvailable(),
                        item.getRequest().getRequestId(),
                        item.getUser().getId()))
                .collect(toList());
        return new RequestAndResponseDto(request.getRequestId(), request.getDescription(), request.getCreated(), itemDtoList);
    }
}
