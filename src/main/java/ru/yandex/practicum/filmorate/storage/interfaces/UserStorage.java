package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    void save(User user);

    void saveOrUpdate(User user);

    User getUser(long userId);

    Collection<User> getUsers();

    Collection<User> getFriends(long userId);
}
