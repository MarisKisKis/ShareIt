package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.user.User;

import static org.hamcrest.Matchers.notNullValue;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

@WebMvcTest(ItemRequestController.class)
@AutoConfigureMockMvc
public class ItemRequestControllerTest {

    @MockBean
    private ItemRequestService itemRequestService;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper mapper;
    private User owner = new User(1L, "owner ", "owner@mail.ru");
    private User user = new User(2L, "user1", "user1@mail.ru");
    private RequestAndResponseDto.ItemDto itemDto = new RequestAndResponseDto.ItemDto(1L, "item1", "items", true, 1L, 1L);
    private DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
    private String format = LocalDateTime.now().format(formatter);
    private LocalDateTime localDateTime = LocalDateTime.parse(format);
    private ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "Описание запроса", LocalDateTime.now());
    private RequestAndResponseDto requestAndResponseDto = new RequestAndResponseDto(1L, "Описание запроса", localDateTime, List.of(itemDto));

    @Test
    void findOwnerRequestsTest() throws Exception {
        when(itemRequestService.createRequest(1L, itemRequestDto))
                .thenReturn(itemRequestDto);
        when(itemRequestService.findOwnerRequests(anyLong()))
                .thenReturn(List.of(requestAndResponseDto));

        mockMvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*]").isArray())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(requestAndResponseDto.getId()), Long.class))
                .andExpect(jsonPath("$[0].description", is(requestAndResponseDto.getDescription())))
                .andExpect(jsonPath("$[0].created", is(requestAndResponseDto.getCreated().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME))))
                .andExpect(jsonPath("$[0].items", hasSize(1)));
    }

    @Test
    void findUserRequestsTest() throws Exception {
        when(itemRequestService.createRequest(2L, itemRequestDto))
                .thenReturn(itemRequestDto);
        when(itemRequestService.findUserRequests(anyLong(), anyInt(), anyInt()))
                .thenReturn(List.of(requestAndResponseDto));

        mockMvc.perform(get("/requests")
                        .characterEncoding(StandardCharsets.UTF_8)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void getRequestTest() throws Exception {
        when(itemRequestService.createRequest(2L, itemRequestDto))
                .thenReturn(itemRequestDto);
        when(itemRequestService.getRequest(1L, itemRequestDto.getId()))
                .thenReturn(requestAndResponseDto);

        mockMvc.perform(get("/users/1", itemRequestDto.getId())
                        .header("X-Sharer-User-Id", user.getId()));
    }

    @Test
    void createRequest() throws Exception {
        when(itemRequestService.createRequest(anyLong(), any()))
                .thenReturn(itemRequestDto);
        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .header("X-Sharer-User-Id", user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(notNullValue())))
                .andExpect(jsonPath("$.description", is(itemRequestDto.getDescription())));

    }
}


