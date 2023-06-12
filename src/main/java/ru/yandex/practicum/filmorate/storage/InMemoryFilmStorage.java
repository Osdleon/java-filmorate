package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.Collection;
import java.util.HashMap;

@Component
public class InMemoryFilmStorage implements FilmStorage {
    HashMap<Long, Film> repository = new HashMap<>();
    int filmId;

    public Film save(Film film) {
        if (film == null)
            return null;
        film.setId(++filmId);
        repository.put(film.getId(), film);
        return film;
    }

    public Film saveOrUpdate(Film film) {
        if (film == null)
            return null;
        if (!repository.containsKey(film.getId()))
            throw new FilmNotFoundException("Film with the id: " + film.getId() + "doesn't exist.");
        repository.put(film.getId(), film);
        return film;
    }

    public Collection<Film> getFilms() {
        return repository.values();
    }

    public void deleteFilmLike(long filmId, long userId) {
        var film = getFilm(filmId);
        var likes = film.getLikes();
        if (likes.contains(userId))
            return;
        likes.remove(userId);
        film.setLikes(likes);
    }

    public void likeFilm(long filmId, long userId) {
        var film = getFilm(filmId);
        var likes = film.getLikes();
        if (likes.contains(userId))
            return;
        likes.add(userId);
        film.setLikes(likes);
    }

    public Film getFilm(long filmId) {
        if (!repository.containsKey(filmId))
            throw new FilmNotFoundException("User with the id: " + filmId + "doesn't exist.");
        return repository.get(filmId);
    }
}
