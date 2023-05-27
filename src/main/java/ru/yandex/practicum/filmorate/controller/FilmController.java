package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.ValidationService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("films")
@Validated
public class FilmController {
    InMemoryFilmStorage storage;
    ValidationService validationService;
    FilmService filmService;

    @Autowired
    public FilmController(InMemoryFilmStorage storage, ValidationService validationService, FilmService filmService) {
        this.storage = storage;
        this.validationService = validationService;
        this.filmService = filmService;
    }

    @PostMapping
    Film saveFilm(@RequestBody @Valid Film film) {
        validationService.validateFilmDate(film);
        storage.save(film);
        return film;
    }

    @PutMapping
    Film updateFilm(@RequestBody @Valid Film film) {
        validationService.validateFilmDate(film);
        storage.saveOrUpdate(film);
        return film;
    }

    @GetMapping
    Collection<Film> getFilms() {
        return storage.getFilms();
    }

    @GetMapping("/{id}")
    Film getUser(@PathVariable long id) {
        return storage.getFilm(id);
    }

    @PutMapping("/{id}/like/{userId}")
    void addLike(@PathVariable long id, @PathVariable long userId) {
        validationService.validateUserId(userId);
        filmService.likeFilm(id, userId, storage);
    }

    @DeleteMapping("/{id}/like/{userId}")
    void deleteLike(@PathVariable long id, @PathVariable long userId) {
        validationService.validateUserId(userId);
        filmService.deleteFilmLike(id, userId, storage);
    }

    @GetMapping("/popular")
    Collection<Film> getMostPopular(@RequestParam Optional<Integer> count) {
        var res = filmService.getMostLikedFilms(count, storage);
        return res;
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUserValidation(final FilmValidationException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleUserValidation(final FilmNotFoundException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleUserValidation(final UserNotFoundException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleUserException(final Exception e) {
        return Map.of("error", e.getMessage());
    }
}
