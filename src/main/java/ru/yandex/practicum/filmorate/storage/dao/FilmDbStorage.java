package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.FilmStorage;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;

@Component("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;
    private static final String insertOperation = "INSERT INTO \"film\" (\"name\", \"description\", \"release_date\", \"duration\") VALUES(?, ?, ?, ?);";
    private static final String insertMpaOperation = "INSERT INTO \"film_mpa\" (\"film_id\", \"mpa_id\") VALUES(?, ?);";
    private static final String insertGenreOperation = "INSERT INTO \"film_genre\" (\"film_id\", \"genre_id\") VALUES(?, ?);";
    private static final String updateOperation = "UPDATE \"film\" SET \"name\" = ?, \"description\"  = ?, \"release_date\"  = ?, \"duration\" = ? WHERE \"id\" = ?";
    private static final String updateMpaOperation = "UPDATE \"film_mpa\" SET \"mpa_id\" = ? WHERE \"film_id\" = ?";
    private static final String updateGenreOperation = "UPDATE \"film_genre\" SET \"genre_id\" = ? WHERE \"film_id\" = ?";
    private static final String getFilmsOperation = "SELECT * FROM \"film\"";
    private static final String removeFilmGenreOperation = "DELETE FROM \"film_genre\" WHERE \"film_id\" = ?;";

    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film save(Film film) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertOperation, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, film.getName());
            ps.setString(2, film.getDescription());
            ps.setDate(3, Date.valueOf(film.getReleaseDate()));
            ps.setLong(4, film.getDuration());
            return ps;
        }, keyHolder);
        for (Genre genre : film.getGenres())
            saveGenre(Objects.requireNonNull(keyHolder.getKey()).intValue(), genre == null ? null : genre.getId());
        saveMpa(Objects.requireNonNull(keyHolder.getKey()).intValue(), film.getMpa() == null ? null : film.getMpa().getId());
        return getFilm(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }


    private void saveMpa(long filmId, Long mpaId) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertMpaOperation, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, filmId);
            ps.setLong(2, mpaId);
            return ps;
        });
    }

    private void saveGenre(long filmId, Long genreId) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertGenreOperation, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, filmId);
            ps.setLong(2, genreId);
            return ps;
        });
    }

    @Override
    public Film getFilm(long filmId) {
        Film film = getFilmIfExist(filmId);
        if (film == null)
            throw new FilmNotFoundException("Film with the id: " + filmId + "doesn't exist.");
        return film;
    }

    public Mpa getMpa(long id) {
        var getMpaOperation = "SELECT * FROM \"mpa\" WHERE \"id\" = ?;";
        return jdbcTemplate.query(getMpaOperation, (rs, rowNum) -> {
                    var mpa = new Mpa();
                    mpa.setId(rs.getLong("id"));
                    mpa.setName(rs.getString("name"));
                    return mpa;
                }, id).stream().findFirst().orElse(null);
    }

    @Override
    public Genre getGenre(long id) {
        var getMpaIdOperation = "SELECT * FROM \"genre\" WHERE \"id\" = ?;";
        return jdbcTemplate.query(getMpaIdOperation, (rs, rowNum) ->
        {
            var genre = new Genre();
            genre.setId(rs.getLong("id"));
            genre.setName(rs.getString("name"));
            return genre;
        }, id).stream().findFirst().orElse(null);
    }

    @Override
    public Collection<Genre> getGenres() {
        var getMpaIdOperation = "SELECT * FROM \"genre\"";
        return jdbcTemplate.query(getMpaIdOperation, (rs, rowNum) ->
        {
            var genre = new Genre();
            genre.setId(rs.getLong("id"));
            genre.setName(rs.getString("name"));
            return genre;
        });
    }

    @Override
    public Collection<Mpa> getMpas() {
        var getMpaOperation = "SELECT * FROM \"mpa\"";
        return jdbcTemplate.query(getMpaOperation, (rs, rowNum) -> {
            var mpa = new Mpa();
            mpa.setId(rs.getLong("id"));
            mpa.setName(rs.getString("name"));
            return mpa;
        });
    }

    Film getFilmIfExist(long filmId) {
        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from \"film\" where \"id\" = ?", filmId);
        if (!filmRows.next())
            return null;
        var film = new Film();
        film.setId(filmRows.getInt("id"));
        film.setName(filmRows.getString("name"));
        Date date = filmRows.getDate("release_date");
        film.setReleaseDate(date != null ? date.toLocalDate() : null);
        film.setDescription(filmRows.getString("description"));
        film.setDuration(filmRows.getLong("duration"));
        var mpa = getFilmMpa(film.getId());
        mpa.ifPresent(film::setMpa);
        var genres = getFilmGenres(filmId);
        film.setGenres(new HashSet<>(genres));
        return film;
    }

    private Film createFilm(ResultSet resultSet) throws SQLException {
        var film = new Film();
        film.setId(resultSet.getInt("id"));
        film.setName(resultSet.getString("name"));
        Date date = resultSet.getDate("release_date");
        film.setReleaseDate(date != null ? date.toLocalDate() : null);
        film.setDuration(resultSet.getLong("duration"));
        film.setDescription(resultSet.getString("description"));
        var mpa = getFilmMpa(film.getId());
        mpa.ifPresent(film::setMpa);
        var genres = getFilmGenres(film.getId());
        film.setGenres(new HashSet<>(genres));
        return film;
    }

    private Optional<Mpa> getFilmMpa(long filmId) {
        var getMpaIdOperation = "SELECT * FROM \"film_mpa\" WHERE \"film_id\" = ?;";
        return jdbcTemplate.query(getMpaIdOperation, (rs, rowNum) ->
                createMpa(rs), filmId).stream().findFirst();
    }

    private Collection<Genre> getFilmGenres(long filmId) {
        var getGenreOperation = "SELECT * FROM \"film_genre\" WHERE \"film_id\" = ?;";
        return jdbcTemplate.query(getGenreOperation, (rs, rowNum) ->
                createGenre(rs), filmId);
    }

    private Mpa createMpa(ResultSet resultSet) throws SQLException {
        var mpa = new Mpa();
        mpa.setId(resultSet.getLong("mpa_id"));
        mpa.setName(getMpaName(mpa.getId()).orElse(null));
        return mpa;
    }

    private Genre createGenre(ResultSet resultSet) throws SQLException {
        var genre = new Genre();
        genre.setId(resultSet.getLong("genre_id"));
        genre.setName(getGenreName(genre.getId()).orElse(null));
        return genre;
    }

    private Optional<String> getGenreName(long genreId) {
        var getMpaIdOperation = "SELECT * FROM \"genre\" WHERE \"id\" = ?;";
        return jdbcTemplate.query(getMpaIdOperation, (rs, rowNum) ->
                rs.getString("name"), genreId).stream().findFirst();
    }

    private Optional<String> getMpaName(long mpaId) {
        var getMpaIdOperation = "SELECT * FROM \"mpa\" WHERE \"id\" = ?;";
        return jdbcTemplate.query(getMpaIdOperation, (rs, rowNum) ->
                rs.getString("name"), mpaId).stream().findFirst();
    }

    @Override
    public Film saveOrUpdate(Film film) {
        if (getFilm(film.getId()) == null)
            throw new FilmNotFoundException("Film with the id: " + film.getId() + "doesn't exist.");
        jdbcTemplate.update(updateOperation, film.getName(), film.getDescription(),
                film.getReleaseDate(), film.getDuration(), film.getId());
        if (film.getMpa() != null)
            jdbcTemplate.update(updateMpaOperation, film.getMpa().getId(), film.getId());
        removeFilmGenres(film.getId());
        for (Genre genre : film.getGenres())
            saveGenre( film.getId(), genre.getId());
        return getFilm(film.getId());
    }

    public void removeFilmGenres(long filmId) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(removeFilmGenreOperation);
            ps.setLong(1, filmId);
            return ps;
        });
    }
    @Override
    public void deleteFilmLike(long filmId, long userId) {
//        removeFilmOperation
//        jdbcTemplate.update(connection -> {
//            PreparedStatement ps = connection.prepareStatement(removeFriendOperation, Statement.RETURN_GENERATED_KEYS);
//            ps.setLong(1, user.getId());
//            ps.setLong(2, friend.getId());
//            return ps;
//        });
    }

    @Override
    public void likeFilm(long filmId, long userId) {

    }

    public Collection<Film> getFilms() {
        return jdbcTemplate.query(getFilmsOperation, (rs, rowNum) -> createFilm(rs));
    }
}
