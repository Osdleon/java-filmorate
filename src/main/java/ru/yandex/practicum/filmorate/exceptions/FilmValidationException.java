package ru.yandex.practicum.filmorate.exceptions;

import javax.validation.ValidationException;

public class FilmValidationException extends ValidationException {
    public FilmValidationException(String message) {
        super(message);
    }
}
