package ru.practicum.booking;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ru.practicum.Create;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingDto {
    private Long id;
    private LocalDateTime start;
    private LocalDateTime end;
    private ItemDto item;
    private UserDto booker;
    private BookingStatus status;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemDto {

        private Long id;
        private String name;
        private String description;
        private Boolean available;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserDto {
        private Long id;
        private String name;
        private String email;
    }
}
