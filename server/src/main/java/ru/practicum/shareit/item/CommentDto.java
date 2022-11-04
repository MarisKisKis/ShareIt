package ru.practicum.shareit.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ru.practicum.shareit.Create;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {
    private Long id;
    @NotBlank(groups = {Create.class})
    private String text;
    @NotBlank(groups = {Create.class})
    private Item item;
    private String authorName;
    @NotBlank(groups = {Create.class})
    private LocalDateTime created;
}
