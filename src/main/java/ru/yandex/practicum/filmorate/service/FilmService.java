package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.GenreNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.MpaNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
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
    public FilmService(@Qualifier("filmDbStorage") FilmStorage filmStorage) {
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

    public Film save(Film film) {
        return this.filmStorage.save(film);
    }

    public Film saveOrUpdate(Film film) {
        return this.filmStorage.saveOrUpdate(film);
    }

    public Collection<Film> getFilms() {
        return this.filmStorage.getFilms();
    }
    public Mpa getMpa(long id){
        var mpa = filmStorage.getMpa(id);
        if (mpa == null)
            throw new MpaNotFoundException();
        return mpa;
    }
    public Genre getGenre(long id){
        var genre = filmStorage.getGenre(id);
        if (genre == null)
            throw new GenreNotFoundException();
        return genre;
    }

    public Film getFilm(long filmId) {
        return this.filmStorage.getFilm(filmId);
    }

    public Collection<Genre> getGenres() {
        return filmStorage.getGenres();
    }

    public Collection<Mpa> getMpas() {
        return filmStorage.getMpas();
    }
}
