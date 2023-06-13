package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.stream.Collectors;

import static ru.yandex.practicum.filmorate.service.UserService.*;

@Component
public class InMemoryUserStorage implements UserStorage {
    HashMap<Long, User> repository = new HashMap<>();
    int userId;

    public User getUser(long userId) {
        if (!repository.containsKey(userId))
            throw new UserNotFoundException("User with the id: " + userId + "doesn't exist.");
        return repository.get(userId);
    }

    public User save(User user) {
        if (user == null) return null;
        user.setId(++userId);
        repository.put(user.getId(), user);
        return user;
    }

    public User update(User user) {
        if (user == null) return null;
        if (!repository.containsKey(user.getId()))
            throw new UserNotFoundException("User with the id: " + user.getId() + "doesn't exist.");
        repository.put(user.getId(), user);
        return user;
    }

    public Collection<User> getUsers() {
        return repository.values();
    }

    public Collection<User> getFriends(long userId) {
        return getUser(userId).getFriends().stream().map(this::getUser).filter(Objects::nonNull).collect(Collectors.toList());
    }

    public void removeFriend(User user, User friend) {
        if (user.getFriends() == null || !user.getFriends().contains(friend.getId()))
            throw new UserNotFoundException(userWithTheId + user.getId() + doesntHaveFriendWithId + friend.getId() + " .");
        if (friend.getFriends() == null || !friend.getFriends().contains(user.getId()))
            throw new UserNotFoundException(userWithTheId + friend.getId() + doesntHaveFriendWithId + user.getId() + " .");
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
        update(user);
        update(friend);
    }

    public void addFriend(User user, User friend) {
        if (user.getFriends() == null) user.setFriends(new HashSet<>());
        else if (user.getFriends().contains(friend.getId()))
            throw new UserNotFoundException(userWithTheId + user.getId() + alreadyHaveFriendWithId + friend.getId() + " .");
        if (friend.getFriends() == null) friend.setFriends(new HashSet<>());
        else if (friend.getFriends().contains(user.getId()))
            throw new UserNotFoundException(userWithTheId + friend.getId() + alreadyHaveFriendWithId + user.getId() + " .");
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
        update(user);
        update(friend);
    }
}