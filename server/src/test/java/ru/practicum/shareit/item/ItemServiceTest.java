package ru.practicum.shareit.item;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest(classes=ItemService.class)
public class ItemServiceTest {
    @Mock
    private ItemService itemService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private BookingRepository bookingRepository;

    private ru.practicum.shareit.user.User owner = new User(1L, "owner", "owner@mail.ru");
    private User user = new User(1L, "user", "java.ru.practicum.user@mail.ru");
    private User user2 = new User(2L, "user2", "user2@mail.ru");
    private Item item = new Item(1L, "item", "some java.ru.practicum.item", true, owner, new ItemRequest(1L, "request", user, LocalDateTime.now()));
    private Item item2 = new Item(2L, "item2", "some item2", true, owner, new ItemRequest(2L, "request2", user2, LocalDateTime.now()));

    private Booking booking1 = new Booking(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusDays(1), item, user, BookingStatus.WAITING);

    private Booking booking2 = new Booking(2L, LocalDateTime.now().plusHours(2), LocalDateTime.now().plusDays(2), item2, user2, BookingStatus.APPROVED);

    private Comment comment = new Comment(1L, "some comment", item, user, LocalDateTime.now().plusDays(1));

    @Test
    void getAllItemsByOwnerTest() {
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(owner));
        when(itemRepository.findAllByUserIdOrderById(anyLong(), any()))
                .thenReturn(List.of(item));
        when(bookingRepository.findByItemIdOrderByStartDesc(anyLong()))
                .thenReturn(List.of(booking1, booking2));
        when(commentRepository.findAllByItemId(anyLong()))
                .thenReturn(List.of(comment));
        List<ItemInfoDto> itemsInfoDto = itemService.getAllItemsByUser(owner.getId(), 0, 20);
        assertNotNull(itemsInfoDto);
        assertEquals(0, itemsInfoDto.size());
    }
}
