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
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String email;
    private String name;
}
