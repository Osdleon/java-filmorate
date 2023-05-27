package ru.yandex.practicum.filmorate.service;

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
    public void likeFilm(long filmId, long userId, FilmStorage filmStorage) {
        filmStorage.likeFilm(filmId, userId);
    }

    public void deleteFilmLike(long filmId, long userId, FilmStorage filmStorage) {
        filmStorage.deleteFilmLike(filmId, userId);
    }

    public Collection<Film> getMostLikedFilms(Optional<Integer> count, FilmStorage filmStorage) {
        var films = filmStorage.getFilms();
        // Здесь лучше сразу хранить в сортированной коллекции.
        TreeSet<Film> sortedFilms = new TreeSet<>(Comparator.comparingInt(
                f -> f.getLikes().size()));
        sortedFilms.addAll(films);
        return sortedFilms.descendingSet().stream().limit(count.orElse(10)).collect(Collectors.toList());
    }
}
