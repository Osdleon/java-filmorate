package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.FilmsRepository;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@RequestMapping("films")
@Validated
public class FilmController {

    FilmsRepository repository;
    ValidationService validationService;
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    public FilmController() {
        repository = new FilmsRepository();
        validationService = new ValidationService();
    }

    @PostMapping
    Film saveFilm(@RequestBody @Valid Film film){
        validationService.validateFilmDate(film);
        repository.save(film);
        return film;
    }

    @PutMapping
    Film updateFilm(@RequestBody @Valid Film film) {
        validationService.validateFilmDate(film);
        repository.saveOrUpdate(film);
        return film;
    }

    @GetMapping
    Collection<Film> getFilms(){
        return repository.getFilms();
    }
}
