package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class InMemoryUserStorage implements UserStorage {
    HashMap<Long, User> repository = new HashMap<>();
    int userId;

    void processUserName(User user) {
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
    }

    public User getUser(long userId) {
        if (!repository.containsKey(userId))
            throw new UserNotFoundException("User with the id: " + userId + "doesn't exist.");
        return repository.get(userId);
    }

    public void save(User user) {
        if (user == null)
            return;
        processUserName(user);
        user.setId(++userId);
        repository.put(user.getId(), user);
    }

    public void saveOrUpdate(User user) {
        if (user == null)
            return;
        if (!repository.containsKey(user.getId()))
            throw new UserNotFoundException("User with the id: " + user.getId() + "doesn't exist.");
        processUserName(user);
        repository.put(user.getId(), user);
    }

    public Collection<User> getUsers() {
        return repository.values();
    }

    public Collection<User> getFriends(long userId) {
        return getUser(userId).getFriends().stream().map(this::getUser).filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}