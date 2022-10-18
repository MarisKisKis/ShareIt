package ru.practicum.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ru.practicum.Create;
import ru.practicum.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class UserDto {
    private Long id;
    @Email(groups = {Update.class, Create.class})
    @NotNull(groups = {Create.class})
    private String email;
    @NotBlank(groups = {Create.class})
    private String name;

}
