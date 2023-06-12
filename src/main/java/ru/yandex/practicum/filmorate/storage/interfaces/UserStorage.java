package ru.yandex.practicum.filmorate.storage.interfaces;

import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;

public interface UserStorage {
    User save(User user);

    User saveOrUpdate(User user);

    User getUser(long userId);

    Collection<User> getUsers();

    Collection<User> getFriends(long userId);

    void addFriend(User user, User friend);
    void removeFriend(User user, User friend);
}
