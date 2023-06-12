package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    Film save(Film film);

    Film saveOrUpdate(Film film);

    void deleteFilmLike(long filmId, long userId);

    void likeFilm(long filmId, long userId);

    Collection<Film> getFilms();

    Film getFilm(long filmId);
}
