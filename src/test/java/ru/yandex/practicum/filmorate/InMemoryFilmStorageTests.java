package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;

public class InMemoryFilmStorageTests {
    @Test
    void saveNullTest() {
        var filmsRepository = new InMemoryFilmStorage();
        filmsRepository.save(null);
        Assertions.assertEquals(0, (long) filmsRepository.getFilms().size());
    }

    @Test
    void saveFilmTest() {
        var filmsRepository = new InMemoryFilmStorage();
        filmsRepository.save(new Film());
        Assertions.assertEquals(1, (long) filmsRepository.getFilms().size());
        Film firstUser = filmsRepository.getFilms().stream().findFirst().orElse(null);
        Assertions.assertNotNull(firstUser);
        Assertions.assertEquals(1, firstUser.getId());
    }

    @Test
    void updateFilmTest() {
        var filmsRepository = new InMemoryFilmStorage();
        filmsRepository.save(new Film());
        var film = new Film();
        film.setName("name");
        film.setDescription("description");
        film.setId(1);
        filmsRepository.saveOrUpdate(film);
        Assertions.assertEquals(1, (long) filmsRepository.getFilms().size());
        Film firstUser = filmsRepository.getFilms().stream().findFirst().orElse(null);
        Assertions.assertNotNull(firstUser);
        Assertions.assertEquals(1, firstUser.getId());
        Assertions.assertEquals("name", firstUser.getName());
        Assertions.assertEquals("description", firstUser.getDescription());
    }
}
