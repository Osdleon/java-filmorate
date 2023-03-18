package ru.yandex.practicum.filmorate.exceptions;

import javax.validation.ValidationException;

public class UserValidationException extends ValidationException {
    public UserValidationException(String message) {
        super(message);
    }
}
