package ru.practicum.request;

import java.util.List;

public interface ItemRequestService {
    List<RequestAndResponseDto> findOwnerRequests(long userId);
    
    List<RequestAndResponseDto> findUserRequests(long userId, Integer from, Integer size);

    RequestAndResponseDto getRequest(long userId, long requestId);

    ItemRequestDto createRequest(long userId, ItemRequestDto requestDto);
}
