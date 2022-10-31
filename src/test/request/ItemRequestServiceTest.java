package request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepository;
import ru.practicum.item.ItemServiceImpl;
import ru.practicum.request.*;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(classes=ItemRequestService.class)
@Transactional
public class ItemRequestServiceTest {

    @Mock
    private ItemRequestService itemRequestService;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private ItemRequestRepository requestRepository;
    private User user = new User(1L, "user1", "user1@mail.ru");

    private ItemRequest request = new ItemRequest(1L, "request", user, LocalDateTime.now());

    private List<Item> itemList = itemRepository.findByRequest_RequestId(request.getRequestId());
    private ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "requestDto", LocalDateTime.now());
    private ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto, user);

    @Test
    void findByRequesterIdTest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(requestRepository.findByRequesterId(anyLong(), any()))
                .thenReturn(List.of(itemRequest));
        when(itemRepository.findByRequest_RequestId(anyLong()))
                .thenReturn(itemList);
        List<RequestAndResponseDto> requestAndResponsesDto = itemRequestService.findOwnerRequests(user.getId());
        assertNotNull(requestAndResponsesDto);
        assertEquals(1, requestAndResponsesDto.size());
        assertEquals(itemRequest.getDescription(), requestAndResponsesDto.get(0).getDescription());
        assertEquals(itemRequest.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
                requestAndResponsesDto.get(0).getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
    }
}
