package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("genres")
@Validated
public class GenreController {
    private static final String error = "error";
    private final FilmService filmService;

    @Autowired
    public GenreController(FilmService filmService) {
        this.filmService = filmService;
    }

    @GetMapping("/{id}")
    Genre getGenres(@PathVariable long id) {
        return filmService.getGenre(id);
    }

    @GetMapping
    Collection<Genre> getGenres() {
        return filmService.getGenres();
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Map<String, String> handleGenreValidation(final GenreNotFoundException e) {
        return Map.of(error, "Genre not found.");
    }
}
