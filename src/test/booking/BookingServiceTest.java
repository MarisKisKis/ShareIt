package booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.booking.*;
import ru.practicum.item.Item;
import ru.practicum.item.ItemRepository;
import ru.practicum.request.ItemRequest;
import ru.practicum.user.User;
import ru.practicum.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@SpringBootTest(classes=BookingService.class)
@Transactional
public class BookingServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private BookingService bookingService;
    private User user = new User(1L, "user1", "user1@mail.ru");
    private final Item item = new Item(1L, "item", "some item", true, user, new ItemRequest(1L, "request", user, LocalDateTime.now()));

    private Booking booking = new Booking(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusDays(1), item, user,
            BookingStatus.WAITING);
    private BookingDto bookingDto = BookingMapper.toBookingDto(booking);
    private BookingDtoInput bookingDtoInput = new BookingDtoInput(item.getId(), LocalDateTime.now().plusHours(1), LocalDateTime.now().plusDays(1), 1);
    @Test
    void testCreateBooking() {
        when(itemRepository.findById(anyLong()))
                .thenReturn(Optional.of(item));
        when(userRepository.findById(anyLong()))
                .thenReturn(Optional.of(user));
        when(bookingRepository.findByItemIdOrderByStartDesc(anyLong()))
                .thenReturn(List.of(booking));
        when(bookingRepository.save(any(Booking.class)))
                .thenReturn(booking);
        BookingDto bookingDtoNew = bookingService.addNewBooking(user.getId(), bookingDtoInput);

        assertNotNull(bookingDtoNew);
        assertEquals(bookingDto.getId(), bookingDtoNew.getId());
        assertEquals(bookingDto.getStart(), bookingDtoNew.getStart());
        assertEquals(bookingDto.getEnd(), bookingDtoNew.getEnd());
        assertEquals(user.getId(), bookingDtoNew.getBooker().getId());
    }

}
