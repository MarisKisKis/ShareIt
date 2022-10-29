package ru.practicum.item;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ru.practicum.Create;
import ru.practicum.user.User;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
