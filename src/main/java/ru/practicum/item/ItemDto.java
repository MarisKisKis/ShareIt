package ru.practicum.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ru.practicum.Create;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ItemDto {
    private Long id;
    @NotBlank(groups = {Create.class})
    private String name;
    @NotBlank(groups = {Create.class})
    private String description;
    private Boolean available;
    //private String request;
}
