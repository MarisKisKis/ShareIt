package ru.practicum.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepository;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = ItemRequestService.class)
public class ItemRequestServiceTest {

    @Mock
    private ItemRequestService itemRequestService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemRequestRepository requestRepository;
    private User user;
    private ItemRequest request;
    private List<Item> itemList;
    private ItemRequestDto itemRequestDto;
    private ItemRequest itemRequest;
    private List<RequestAndResponseDto> requestAndResponsesDto;

    @BeforeEach
    void beforeEach() {
        user = new User(1L, "user1", "user1@mail.ru");

        request = new ItemRequest(1L, "request", user, LocalDateTime.now());

        itemList = itemRepository.findByRequest_RequestId(request.getRequestId());
        itemRequestDto = new ItemRequestDto(1L, "requestDto", LocalDateTime.now());
        itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, user);
        requestAndResponsesDto = itemRequestService.findOwnerRequests(1L);
    }

    @Test
    void findByRequesterIdTest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(requestRepository.findByRequesterId(anyLong(), any()))
                .thenReturn(List.of(itemRequest));
        when(itemRepository.findByRequest_RequestId(anyLong()))
                .thenReturn(itemList);
        assertNotNull(requestAndResponsesDto);
    }
}
