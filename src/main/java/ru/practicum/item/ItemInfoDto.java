package ru.practicum.item;

import java.time.LocalDateTime;

public class ItemInfoDto {

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private BookingDto lastBooking;
    private BookingDto nextBooking;

    public static class BookingDto {
        Long id;
        LocalDateTime start;
		LocalDateTime end;
        Long bookerId;
    }
}
