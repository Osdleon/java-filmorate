package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserDbStorage;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class UserDbStorageTests {
    private final UserDbStorage userStorage;

    private void createUser() {
        var testUser = new User();
        testUser.setName("name");
        testUser.setLogin("login");
        var date = LocalDate.parse("20230101", DateTimeFormatter.BASIC_ISO_DATE);
        testUser.setBirthday(date);
        testUser.setEmail("aaa@bbb.ru");
        userStorage.save(testUser);
    }

    private void createUser1() {
        createUser1(1, false);
    }

    private void createUser1(Integer id) {
        createUser1(id, false);
    }

    private void createUser1(Integer id, boolean update) {
        var testUser = new User();
        testUser.setName("name1");
        testUser.setLogin("login1");
        var date = LocalDate.parse("20230101", DateTimeFormatter.BASIC_ISO_DATE);
        testUser.setBirthday(date);
        testUser.setEmail("aaa@bbb.ru");
        if (id != null)
            testUser.setId(id);
        if (update)
            userStorage.update(testUser);
        else
            userStorage.save(testUser);
    }

    private void checkUser(User user) {
        var date = LocalDate.parse("20230101", DateTimeFormatter.BASIC_ISO_DATE);
        Assertions.assertEquals("name", user.getName());
        Assertions.assertEquals("login", user.getLogin());
        Assertions.assertEquals("aaa@bbb.ru", user.getEmail());
        Assertions.assertEquals(date, user.getBirthday());

    }

    private void checkUser1(User user) {
        var date = LocalDate.parse("20230101", DateTimeFormatter.BASIC_ISO_DATE);
        Assertions.assertEquals("name1", user.getName());
        Assertions.assertEquals("login1", user.getLogin());
        Assertions.assertEquals("aaa@bbb.ru", user.getEmail());
        Assertions.assertEquals(date, user.getBirthday());
    }

    @Test
    void testFindUserById() {
        createUser();
        var user = userStorage.getUser(1);
        checkUser(user);
    }

    @Test
    void testFindUser1ById() {
        createUser1();
        var user = userStorage.getUser(1);
        checkUser1(user);
    }

    @Test
    void testUpdateUser() {
        createUser();
        createUser1(1, true);
        var user = userStorage.getUser(1);
        checkUser1(user);
    }

    @Test
    void testGetUsers() {
        createUser();
        createUser1(2);
        var users = userStorage.getUsers();
        Assertions.assertEquals(2, users.size());
        checkUser(Objects.requireNonNull(users.stream().findFirst().orElse(null)));
        checkUser1(Objects.requireNonNull(users.stream().skip(1).findFirst().orElse(null)));
    }

    @Test
    void testAddGetUsers() {
        createUser();
        createUser1(2);
        var users = userStorage.getUsers();
        Assertions.assertEquals(2, users.size());
        checkUser(Objects.requireNonNull(users.stream().findFirst().orElse(null)));
        checkUser1(Objects.requireNonNull(users.stream().skip(1).findFirst().orElse(null)));
    }

    @Test
    void testAddGetFriends() {
        createUser();
        createUser1(2);
        var user = userStorage.getUser(1);
        var user1 = userStorage.getUser(2);
        userStorage.addFriend(user, user1);
        var friends = userStorage.getFriends(1);
        Assertions.assertEquals(1, friends.size());
        checkUser1(Objects.requireNonNull(friends.stream().findFirst().orElse(null)));
    }
}