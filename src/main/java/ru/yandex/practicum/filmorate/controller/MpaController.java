package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;
import java.util.Map;

@RestController
@RequestMapping("mpa")
@Validated
public class MpaController {
    private static final String error = "error";
    private final FilmService filmService;
    @Autowired
    public MpaController(FilmService filmService) {
        this.filmService = filmService;
    }
    @GetMapping("/{id}")
    Mpa getGenres(@PathVariable long id) {
        return filmService.getMpa(id);
    }
    @GetMapping
    Collection<Mpa> getGenres() {
        return filmService.getMpas();
    }
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    Map<String, String> handleMpaValidation(final MpaNotFoundException e) {
        return Map.of(error, "Mpa not found.");
    }
}
