package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmsRepository;

import javax.validation.Validation;
import javax.validation.Validator;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class FilmTests {
    private static Validator validator;

    static {
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }
    Film createTestFilm(){
        var film =  new Film();
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
        int targetStringLength = 201;
        Random random = new Random();
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z's
        StringBuilder buffer = new StringBuilder(targetStringLength);
        for (int i = 0; i < targetStringLength; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        String generatedString = buffer.toString();
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
