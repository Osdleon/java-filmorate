package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.Collection;
import java.util.Comparator;
import java.util.Optional;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class FilmService {
    FilmStorage filmStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage) {
        this.filmStorage = filmStorage;
    }

    public void likeFilm(long filmId, long userId) {
        filmStorage.likeFilm(filmId, userId);
    }

    public void deleteFilmLike(long filmId, long userId) {
        filmStorage.deleteFilmLike(filmId, userId);
    }

    public Collection<Film> getMostLikedFilms(Optional<Integer> count) {
        var films = filmStorage.getFilms();
        TreeSet<Film> sortedFilms = new TreeSet<>(Comparator.comparingInt(
                f -> f.getLikes().size()));
        sortedFilms.addAll(films);
        return sortedFilms.descendingSet().stream().limit(count.orElse(10)).collect(Collectors.toList());
    }

    public void save(Film film) {
        this.filmStorage.save(film);
    }

    public void saveOrUpdate(Film film) {
        this.filmStorage.saveOrUpdate(film);
    }

    public Collection<Film> getFilms() {
        return this.filmStorage.getFilms();
    }

    public Film getFilm(long filmId) {
        return this.filmStorage.getFilm(filmId);
    }
}
