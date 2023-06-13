package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

public class InMemoryUserStorageTests {
    @Test
    void saveNullTest() {
        var usersRepository = new InMemoryUserStorage();
        usersRepository.save(null);
        Assertions.assertEquals(0, (long) usersRepository.getUsers().size());
    }

    @Test
    void saveUserTest() {
        var usersRepository = new InMemoryUserStorage();
        usersRepository.save(new User());
        Assertions.assertEquals(1, (long) usersRepository.getUsers().size());
        User firstUser = usersRepository.getUsers().stream().findFirst().orElse(null);
        Assertions.assertNotNull(firstUser);
        Assertions.assertEquals(1, firstUser.getId());
    }

    @Test
    void updateUserTest() {
        var usersRepository = new InMemoryUserStorage();
        usersRepository.save(new User());
        var user = new User();
        user.setLogin("login");
        user.setName("name");
        user.setId(1);
        usersRepository.update(user);
        Assertions.assertEquals(1, (long) usersRepository.getUsers().size());
        User firstUser = usersRepository.getUsers().stream().findFirst().orElse(null);
        Assertions.assertNotNull(firstUser);
        Assertions.assertEquals(1, firstUser.getId());
        Assertions.assertEquals("name", firstUser.getName());
        Assertions.assertEquals("login", firstUser.getLogin());
    }
}
