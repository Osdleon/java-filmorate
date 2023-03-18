package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ValidationService {
    LocalDate limitDate;
    public ValidationService() {
        limitDate = LocalDate.parse("18951228", DateTimeFormatter.BASIC_ISO_DATE);
    }

    public void validateFilmDate(Film film) {
        var date = film.getReleaseDate();
        if (date == null)
            throw new FilmValidationException("Film release date is null.");
        if (date.isBefore(limitDate))
            throw new FilmValidationException("Film release date is incorrect.");
    }
}
