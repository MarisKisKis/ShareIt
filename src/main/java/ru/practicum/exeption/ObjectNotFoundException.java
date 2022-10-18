package ru.practicum.exeption;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class ObjectNotFoundException extends RuntimeException {
    public ObjectNotFoundException(String message) {
        super(message);
    }
}
