package ru.practicum.exeption;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ErrorResponse extends Throwable {
    public ErrorResponse(String message) {
        super(message);
    }
}
