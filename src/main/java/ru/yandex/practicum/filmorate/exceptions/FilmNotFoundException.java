package ru.yandex.practicum.filmorate.exceptions;

import java.util.NoSuchElementException;

public class FilmNotFoundException extends NoSuchElementException {
    public FilmNotFoundException(String message) {
        super(message);
    }
}
