package ru.practicum.shareit.request;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestDto {

    private long id;
    private String description;
    private LocalDateTime created;
    private List<ItemDto> items;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemDto {
        private long id;
        private String name;
        private String description;
        private Boolean available;
        private long requestId;
    }
}
