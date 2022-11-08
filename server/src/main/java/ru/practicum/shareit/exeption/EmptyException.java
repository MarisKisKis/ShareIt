package ru.practicum.shareit.exeption;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EmptyException extends RuntimeException {
    public EmptyException(String message) {
        super(message);
    }
}
