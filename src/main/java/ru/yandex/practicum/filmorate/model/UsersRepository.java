package ru.yandex.practicum.filmorate.model;

import ru.yandex.practicum.filmorate.exceptions.UserValidationException;

import java.util.Collection;
import java.util.HashMap;

public class UsersRepository {
    HashMap<Integer, User> repository = new HashMap<>();
    int userId;

    public UsersRepository() {
    }

    void processUserName(User user) {
        if (user.name == null || user.name.isBlank())
            user.name = user.login;
    }

    public void save(User user) {
        if (user == null)
            return;
        processUserName(user);
        user.setId(++userId);
        repository.put(user.id, user);
    }

    public void saveOrUpdate(User user) {
        if (user == null)
            return;
        if (!repository.containsKey(user.id))
            throw new UserValidationException("User with the id: " + user.id + "doesn't exist.");
        processUserName(user);
        repository.put(user.id, user);
    }

    public Collection<User> getUsers() {
        return repository.values();
    }
}