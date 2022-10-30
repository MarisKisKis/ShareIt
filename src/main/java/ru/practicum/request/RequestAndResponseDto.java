package ru.practicum.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestAndResponseDto {
    private Long id;
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ItemDto {
        private Long id;
        private String name;
        private String description;
        private Boolean available;
        private Long requestId;
        private Long ownerId;
    }
}
