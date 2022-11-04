package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.user.User;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(BookingController.class)
@AutoConfigureMockMvc
public class BookingControllerTest {

    @MockBean
    private BookingService bookingService;

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;

    private User user = new User(1L, "user1", "user1@mail.ru");
    private User owner = new User(2L, "user2", "user2@mail.ru");

    private final Item item = new Item(1L, "item", "some java.ru.practicum.item", true, user, new ItemRequest(1L, "request", user, LocalDateTime.now()));
    private Booking booking = new Booking(1L, LocalDateTime.now().plusHours(1), LocalDateTime.now().plusDays(1), item, user,
            BookingStatus.WAITING);
    private final BookingDto bookingDto = BookingMapper.toBookingDto(booking);

    @Test
    void findBookingByIdTest() throws Exception {
        when(bookingService.findBookingById(anyLong(), anyLong())).thenReturn(bookingDto);
        mockMvc.perform(get("/bookings/{id}", booking.getId())
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect((ResultMatcher) jsonPath("$.item.name", is(booking.getItem().getName())));
    }

    @Test
    void getUserBookingsTest() throws Exception {
        when(bookingService.getUserBookings(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .characterEncoding(StandardCharsets.UTF_8))
                .andExpect(status().isOk());
    }

    @Test
    void getAllItemsBookingsTest() throws Exception {
        when(bookingService.getAllItemsBookings(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(bookingDto));

        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 2))
                .andExpect(status().isOk());
    }

    @Test
    void addBookingTest() throws Exception {
        when(bookingService.addNewBooking(anyLong(), any())).thenReturn(bookingDto);
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(BookingMapper.toBookingDto(booking)))
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id", is(bookingDto.getId()), Long.class))
                .andExpect((ResultMatcher) jsonPath("$.item.name", is(booking.getItem().getName())));
    }

    @Test
    void approveBookingTest() throws Exception {
        when(bookingService.approveBooking(anyLong(), anyLong(), anyString()))
                .thenReturn(bookingDto);
        mockMvc.perform(patch("/bookings/1")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1)
                        .param("approved", "true")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect((ResultMatcher) jsonPath("$.id", is(bookingDto.getId()), Long.class));
    }
}
