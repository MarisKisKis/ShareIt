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
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BookingDto {
    private long bookingId;
    @NotBlank(groups = {Create.class})
    private LocalDateTime start;
    private LocalDateTime end;
    @NotBlank(groups = {Create.class})
    private long itemId;
    @NotBlank(groups = {Create.class})
    private long bookerId;
    private BookingStatus status;
}
