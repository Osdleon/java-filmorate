package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;

public interface FilmStorage {
    void save(Film film);

    void saveOrUpdate(Film film);

    Collection<Film> getFilms();

    void deleteFilmLike(long filmId, long userId);

    void likeFilm(long filmId, long userId);
}