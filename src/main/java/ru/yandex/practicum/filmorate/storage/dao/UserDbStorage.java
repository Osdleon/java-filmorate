package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;

@Component("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void save(User user) {
        jdbcTemplate.update("INSERT INTO \"user\"" +
                        " (\"email\", \"login\", \"name\", \"birthday\")" +
                        " VALUES(?, ?, ?, ?);",
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
    }

    @Override
    public void saveOrUpdate(User user) {
        jdbcTemplate.update("INSERT INTO \"user\"" +
                        " (\"email\", \"login\", \"name\", \"birthday\")" +
                        " VALUES(?, ?, ?, ?);",
                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
    }

    @Override
    public User getUser(long userId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from \"user\" where id = ?", userId);
        if (userRows.next()) {
            var user = new User();
            user.setId(userRows.getInt("id"));
            user.setName(userRows.getString("name"));
            Date date = userRows.getDate("birthday");
            user.setBirthday(date != null ? date.toLocalDate() : null);
            user.setLogin(userRows.getString("login"));
            user.setEmail(userRows.getString("email"));
            return user;
        }
        return null;
    }

    @Override
    public Collection<User> getUsers() {
        var sql = "select * from user order by name";
        return jdbcTemplate.query(sql, (rs, rowNum) -> createUser(rs));
    }

    private User createUser(ResultSet resultSet) throws SQLException {
        var user = new User();
        user.setId(resultSet.getInt("id"));
        user.setName(resultSet.getString("name"));
        Date date = resultSet.getDate("birthday");
        user.setBirthday(date != null ? date.toLocalDate() : null);
        user.setLogin(resultSet.getString("login"));
        user.setEmail(resultSet.getString("email"));
        return user;
    }

    @Override
    public Collection<User> getFriends(long userId) {
        var sql = "SELECT u2.id AS id, u2.email AS email, u2.name AS name," +
                " u2.birthday AS birthday, u2.login AS login" +
                " FROM user AS u" +
                " INNER JOIN friends f ON f.user_id = u.id" +
                " INNER JOIN user u2 ON f.friend_id = u2.id" +
                " WHERE f.user_id = ? AND f.status = TRUE";
        return jdbcTemplate.query(sql, (rs, rowNum) -> createUser(rs), userId);
    }
}
