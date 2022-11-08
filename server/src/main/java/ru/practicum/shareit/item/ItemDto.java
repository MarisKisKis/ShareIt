package ru.practicum.shareit.item;

import lombok.*;
import ru.practicum.shareit.Create;

import javax.validation.constraints.NotBlank;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class ItemDto {
    private long id;
    @NotBlank(groups = {Create.class})
    private String name;
    @NotBlank(groups = {Create.class})
    private String description;
    private Boolean available;
    private long requestId;

}
