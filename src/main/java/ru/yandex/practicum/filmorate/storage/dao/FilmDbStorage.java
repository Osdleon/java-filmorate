package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.util.Collection;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    public FilmDbStorage(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate=jdbcTemplate;
    }
    @Override
    public void save(Film film) {

    }

    @Override
    public void saveOrUpdate(Film film) {

    }

    @Override
    public void deleteFilmLike(long filmId, long userId) {

    }

    @Override
    public void likeFilm(long filmId, long userId) {

    }

    @Override
    public Collection<Film> getFilms() {
        return null;
    }

    @Override
    public Film getFilm(long filmId) {
        return null;
    }
}
