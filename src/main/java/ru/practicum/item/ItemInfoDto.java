package ru.practicum.item;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemInfoDto {

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentDto> commentsDto;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BookingDto {
        Long bookingId;
        LocalDateTime start;
		LocalDateTime end;
        Long bookerId;
    }
}
