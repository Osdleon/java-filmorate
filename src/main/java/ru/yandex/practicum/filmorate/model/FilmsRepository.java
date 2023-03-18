package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.exceptions.UserValidationException;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.HashMap;

public class FilmsRepository {
    HashMap<Integer, Film> repository = new HashMap<>();
    int filmId;
    public FilmsRepository() {
    }
    public void save(Film film) {
        film.setId(++filmId);
        repository.put(film.id, film);
    }
    public void saveOrUpdate(Film film) {
        if (!repository.containsKey(film.id))
            throw new UserValidationException("Film with the id: " + film.id + "doesn't exist.");
        repository.put(film.id, film);
    }
    public Collection<Film> getFilms() {
        return repository.values();
    }
}
