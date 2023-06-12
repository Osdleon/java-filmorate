package ru.yandex.practicum.filmorate.storage.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.sql.*;
import java.util.Collection;
import java.util.Objects;

@Component("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;
    //private final SimpleJdbcInsert insertIntoUser;
    private static final String insertOperation = "INSERT INTO \"user\" (\"email\", \"login\", \"name\", \"birthday\") VALUES(?, ?, ?, ?);";
    private static final String updateOperation = "UPDATE \"user\" SET \"email\" = ?, \"login\"  = ?, \"name\"  = ?, \"birthday\" = ? WHERE \"id\" = ?";
    private static final String addFriendOperation = "INSERT INTO \"friends\" (\"user_id\", \"friend_id\", \"status\") VALUES(?, ?, ?);";
    private static final String getFriendshipOperation = "SELECT \"friends\" WHERE \"user_id\" = ? AND \"friend_id\" = ?;";
    private static final String removeFriendOperation = "DELETE FROM \"friends\" WHERE \"user_id\" = ? AND \"friend_id\" = ?;";

    public UserDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        //insertIntoUser = new SimpleJdbcInsert(jdbcTemplate).withTableName("user").usingGeneratedKeyColumns("id");
    }

    @Override
    public User save(User user) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(insertOperation, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, user.getEmail());
            ps.setString(2, user.getLogin());
            ps.setString(3, user.getName());
            ps.setDate(4, Date.valueOf(user.getBirthday()));
            return ps;
        }, keyHolder);
        return getUser(Objects.requireNonNull(keyHolder.getKey()).intValue());
    }

    @Override
    public User saveOrUpdate(User user) {
        if (getUser(user.getId()) == null)
            throw new UserNotFoundException("User with the id: " + user.getId() + "doesn't exist.");
        jdbcTemplate.update(updateOperation, user.getEmail(), user.getLogin(), user.getName(), user.getBirthday(), user.getId());
        return getUser(user.getId());
    }

    @Override
    public User getUser(long userId) {
        User user = getUserIfExist(userId);
        if (user == null)
            throw new UserNotFoundException("User with the id: " + userId + "doesn't exist.");
        return user;
    }


    User getUserIfExist(long userId) {
        SqlRowSet userRows = jdbcTemplate.queryForRowSet("select * from \"user\" where \"id\" = ?", userId);
        if (!userRows.next())
            return null;
        var user = new User();
        user.setId(userRows.getInt("id"));
        user.setName(userRows.getString("name"));
        Date date = userRows.getDate("birthday");
        user.setBirthday(date != null ? date.toLocalDate() : null);
        user.setLogin(userRows.getString("login"));
        user.setEmail(userRows.getString("email"));
        return user;
    }

    @Override
    public Collection<User> getUsers() {
        var sql = "select * from \"user\" order by \"name\"";
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
        var sql = "SELECT u2.\"id\" AS id, u2.\"email\" AS email, u2.\"name\" AS name," + " u2.\"birthday\" AS birthday, u2.\"login\" AS login" + " FROM \"user\" AS u" + " INNER JOIN \"friends\" f ON f.\"user_id\" = u.\"id\"" + " INNER JOIN \"user\" u2 ON f.\"friend_id\" = u2.\"id\"" + " WHERE f.\"user_id\" = ? AND f.\"status\" = TRUE";
        return jdbcTemplate.query(sql, (rs, rowNum) -> createUser(rs), userId);
    }

    public Collection<Boolean> getFriendship(long userId, long friendId) {
        return jdbcTemplate.query(getFriendshipOperation, (rs, rowNum) ->
                rs.getBoolean("status"), userId, friendId);
    }

    //    @Override
//    public void save(User user) {
//        jdbcTemplate.update("INSERT INTO \"user\"" +
//                        " (\"email\", \"login\", \"name\", \"birthday\")" +
//                        " VALUES(?, ?, ?, ?);",
//                user.getEmail(), user.getLogin(), user.getName(), user.getBirthday());
//    }
    public void addFriend(User user, User friend) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(addFriendOperation, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, user.getId());
            ps.setLong(2, friend.getId());
            ps.setBoolean(3, true);
            return ps;
        });
    }
    public void removeFriend(User user, User friend) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(removeFriendOperation, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, user.getId());
            ps.setLong(2, friend.getId());
            return ps;
        });
    }
}
