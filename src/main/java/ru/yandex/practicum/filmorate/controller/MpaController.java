package ru.yandex.practicum.filmorate.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("mpa")
@Validated
public class MpaController {
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
}
