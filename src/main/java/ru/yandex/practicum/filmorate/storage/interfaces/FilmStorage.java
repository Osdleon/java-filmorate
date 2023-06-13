package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.Collection;

public interface FilmStorage {
    Film save(Film film);

    Film update(Film film);

    void deleteFilmLike(long filmId, long userId);

    void likeFilm(long filmId, long userId);

    Collection<Film> getFilms();

    Film getFilm(long filmId);

    Mpa getMpa(long filmId);

    Genre getGenre(long id);

    Collection<Genre> getGenres();

    Collection<Mpa> getMpas();
}
