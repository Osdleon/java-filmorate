package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.UsersRepository;

public class UsersRepositoryTests {
    @Test
    void saveUserTest() {
        var usersRepository = new UsersRepository();
        usersRepository.save(null);
        Assertions.assertEquals(0, usersRepository.getUsers().stream().count());
    }
}
