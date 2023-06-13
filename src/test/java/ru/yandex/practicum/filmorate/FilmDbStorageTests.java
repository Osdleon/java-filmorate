package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.dao.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.dao.UserDbStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class FilmDbStorageTests {
    private final FilmDbStorage filmStorage;
    private final UserDbStorage userStorage;

    private void createFilm() {
        var testFilm = new Film();
        testFilm.setName("name");
        testFilm.setDuration(111);
        var date = LocalDate.parse("20230101", DateTimeFormatter.BASIC_ISO_DATE);
        testFilm.setReleaseDate(date);
        testFilm.setDescription("d1");
        var mpa = new Mpa();
        mpa.setId(1);
        testFilm.setMpa(mpa);
        var genre = new Genre();
        genre.setId(1);
        testFilm.setGenres(new HashSet<>(List.of(genre)));
        filmStorage.save(testFilm);
    }

    private void createFilm1() {
        createFilm1(1, false);
    }

    private void createFilm1(Integer id) {
        createFilm1(id, false);
    }

    private void createFilm1(Integer id, boolean update) {
        var testFilm = new Film();
        testFilm.setName("name2");
        testFilm.setDuration(222);
        var date = LocalDate.parse("20230101", DateTimeFormatter.BASIC_ISO_DATE);
        testFilm.setReleaseDate(date);
        testFilm.setDescription("d2");
        var mpa = new Mpa();
        mpa.setId(2);
        testFilm.setMpa(mpa);
        if (id != null)
            testFilm.setId(id);
        if (update)
            filmStorage.update(testFilm);
        else
            filmStorage.save(testFilm);
    }

    private void createUser() {
        var testUser = new User();
        testUser.setName("name");
        testUser.setLogin("login");
        var date = LocalDate.parse("20230101", DateTimeFormatter.BASIC_ISO_DATE);
        testUser.setBirthday(date);
        testUser.setEmail("aaa@bbb.ru");
        userStorage.save(testUser);
    }

    private void checkFilm(Film film) {
        var date = LocalDate.parse("20230101", DateTimeFormatter.BASIC_ISO_DATE);
        Assertions.assertEquals("name", film.getName());
        Assertions.assertEquals("d1", film.getDescription());
        Assertions.assertEquals(111, film.getDuration());
        Assertions.assertEquals(date, film.getReleaseDate());
        Assertions.assertEquals(1, film.getMpa().getId());
        Assertions.assertEquals("G", film.getMpa().getName());
    }

    private void checkFilm1(Film film) {
        var date = LocalDate.parse("20230101", DateTimeFormatter.BASIC_ISO_DATE);
        Assertions.assertEquals("name2", film.getName());
        Assertions.assertEquals("d2", film.getDescription());
        Assertions.assertEquals(222, film.getDuration());
        Assertions.assertEquals(date, film.getReleaseDate());
        Assertions.assertEquals(2, film.getMpa().getId());
        Assertions.assertEquals("PG", film.getMpa().getName());
    }

    @Test
    public void testFindFilmById() {
        createFilm();
        var film = filmStorage.getFilm(1);
        checkFilm(film);
    }

    @Test
    public void testFindFilm1ById() {
        createFilm1();
        var film = filmStorage.getFilm(1);
        checkFilm1(film);
    }

    @Test
    public void testUpdateFilm() {
        createFilm();
        createFilm1(1, true);
        var film = filmStorage.getFilm(1);
        checkFilm1(film);
    }

    @Test
    public void testGetFilms() {
        createFilm();
        createFilm1(2);
        var films = filmStorage.getFilms();
        Assertions.assertEquals(2, films.size());
        checkFilm(Objects.requireNonNull(films.stream().findFirst().orElse(null)));
        checkFilm1(Objects.requireNonNull(films.stream().skip(1).findFirst().orElse(null)));
    }

    @Test
    public void testGetMpa() {
        createFilm1();
        var film = filmStorage.getFilm(1);
        checkFilm1(film);
    }

    @Test
    public void testGetMpas() {
        var mpas = filmStorage.getMpas();
        Assertions.assertEquals(5, mpas.size());
        var mpa = mpas.stream().findFirst().orElse(null);
        Assertions.assertNotNull(mpa);
        Assertions.assertEquals(1, mpa.getId());
        Assertions.assertEquals("G", mpa.getName());

        var mpa1 = mpas.stream().skip(1).findFirst().orElse(null);
        Assertions.assertNotNull(mpa1);
        Assertions.assertEquals(2, mpa1.getId());
        Assertions.assertEquals("PG", mpa1.getName());
    }

    @Test
    public void testGenres() {
        var genres = filmStorage.getGenres();
        Assertions.assertEquals(6, genres.size());
        var genre = genres.stream().findFirst().orElse(null);
        Assertions.assertNotNull(genre);
        Assertions.assertEquals(1, genre.getId());
        Assertions.assertEquals("Комедия", genre.getName());

        var genre1 = genres.stream().skip(1).findFirst().orElse(null);
        Assertions.assertNotNull(genre1);
        Assertions.assertEquals(2, genre1.getId());
        Assertions.assertEquals("Драма", genre1.getName());
    }

    @Test
    public void testGetGenre() {
        var genre = filmStorage.getGenre(4);
        Assertions.assertNotNull(genre);
        Assertions.assertEquals(4, genre.getId());
        Assertions.assertEquals("Триллер", genre.getName());
    }

    @Test
    public void testLikeFilm() {
        createUser();
        createFilm();
        filmStorage.likeFilm(1, 1);
        var film = filmStorage.getFilm(1);
        var likes = film.getLikes();
        Assertions.assertEquals(1, likes.size());
        var like = likes.stream().findFirst().orElse(null);
        Assertions.assertNotNull(like);
        Assertions.assertEquals(1, like);
    }

    @Test
    public void testDeleteLikeFilm() {
        createUser();
        createFilm();
        filmStorage.likeFilm(1, 1);
        filmStorage.deleteFilmLike(1, 1);
        var film = filmStorage.getFilm(1);
        var likes = film.getLikes();
        Assertions.assertEquals(0, likes.size());
    }
}