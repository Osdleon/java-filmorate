package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
public class UserService {
    public static final String userWithTheId = "User with the id: ";
    private static final String userFriendWithTheId = "User friend with the id: ";
    private static final String doesntExist = "doesn't exist.";
    public static final String alreadyHaveFriendWithId = "already have friend with id:";
    public static final String doesntHaveFriendWithId = "doesn't have friend with id:";
    UserStorage storage;

    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage storage) {
        this.storage = storage;
    }

    public void addFriend(Long userId, Long friendId) {
        var user = storage.getUser(userId);
        if (user == null)
            throw new UserNotFoundException(userWithTheId + userId + doesntExist);
        var friend = storage.getUser(friendId);
        if (friend == null)
            throw new UserNotFoundException(userFriendWithTheId + friendId + doesntExist);
        storage.addFriend(user, friend);
    }

    public void removeFriend(Long userId, Long friendId) {
        var user = storage.getUser(userId);
        if (user == null)
            throw new UserNotFoundException(userWithTheId + userId + doesntExist);
        var friend = storage.getUser(friendId);
        if (friend == null)
            throw new UserNotFoundException(userFriendWithTheId + friendId + doesntExist);
        storage.removeFriend(user, friend);
    }

    public Collection<User> getCommonFriends(Long id, Long otherId) {
        var userFriends = storage.getFriends(id);
        var otherUserFriends = storage.getFriends(otherId);
        return userFriends.stream()
                .distinct()
                .filter(otherUserFriends::contains)
                .collect(Collectors.toSet());
    }

    public User save(User user) {
        processUserName(user);
        return this.storage.save(user);
    }

    public User saveOrUpdate(User user) {
        processUserName(user);
        return this.storage.update(user);
    }

    void processUserName(User user) {
        if (user.getName() == null || user.getName().isBlank())
            user.setName(user.getLogin());
    }

    public User getUser(long userId) {
        var user = this.storage.getUser(userId);
        if (user == null)
            throw new UserNotFoundException("User with the id: " + userId + "doesn't exist.");
        return user;
    }

    public Collection<User> getUsers() {
        return this.storage.getUsers();
    }

    public Collection<User> getFriends(long userId) {
        return this.storage.getFriends(userId);
    }
}
