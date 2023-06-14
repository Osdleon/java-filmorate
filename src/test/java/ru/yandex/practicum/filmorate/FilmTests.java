package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

class FilmTests {
    private static final Validator validator;

    public static String getLongString(int targetStringLength) {
        Random random = new Random();
        int leftLimit = 97;
        int rightLimit = 122;
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    static {
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }

    Film createTestFilm() {
        var film = new Film();
        film.setId(1);
        film.setDescription("description");
        film.setName("name");
        film.setDuration(123);
        film.setReleaseDate(LocalDate.parse("20230101", DateTimeFormatter.BASIC_ISO_DATE));
        return film;
    }

    @Test
    void validateFilm() {
        var film = createTestFilm();
        var violations = validator.validate(film);
        Assertions.assertEquals(0, violations.size());
    }

    @Test
    void validateTooLongLogin() {
        var film = createTestFilm();
        String generatedString = getLongString(201);
        film.setDescription(generatedString);
        var violations = validator.validate(film);
        Assertions.assertEquals(1, violations.size());
    }

    @Test
    void validateFilmDuration() {
        var film = createTestFilm();
        film.setDuration(-1);
        var violations = validator.validate(film);
        Assertions.assertEquals(1, violations.size());
    }

    @Test
    void validateFilmEmptyName() {
        var film = createTestFilm();
        film.setName("");
        var violations = validator.validate(film);
        Assertions.assertEquals(1, violations.size());
    }
}
