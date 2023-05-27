package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
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
    public void validateUserId(long id) {
        if (id < 0)
            throw new UserNotFoundException("User id is incorrect.");
    }
}
