package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final String USER_WITH_THE_ID = "User with the id: ";
    private static final String USER_FRIEND_WITH_THE_ID = "User friend with the id: ";
    private static final String DOESN_T_EXIST = "doesn't exist.";
    private static final String ALREADY_HAVE_FRIEND_WITH_ID = "already have friend with id:";
    private static final String DOESN_T_HAVE_FRIEND_WITH_ID = "doesn't have friend with id:";
    InMemoryUserStorage storage;

    @Autowired
    public UserService(InMemoryUserStorage storage) {
        this.storage = storage;
    }

    void addFriend(User user, User friend) {
        if (user.getFriends() == null)
            user.setFriends(new HashSet<>());
        else if (user.getFriends().contains(friend.getId()))
            throw new UserNotFoundException(USER_WITH_THE_ID + user.getId() + ALREADY_HAVE_FRIEND_WITH_ID
                    + friend.getId() + " .");
        if (friend.getFriends() == null)
            friend.setFriends(new HashSet<>());
        else if (friend.getFriends().contains(user.getId()))
            throw new UserNotFoundException(USER_WITH_THE_ID + friend.getId() + ALREADY_HAVE_FRIEND_WITH_ID
                    + user.getId() + " .");
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
    }

    void removeFriend(User user, User friend) {
        if (user.getFriends() == null || !user.getFriends().contains(friend.getId()))
            throw new UserNotFoundException(USER_WITH_THE_ID + user.getId() + DOESN_T_HAVE_FRIEND_WITH_ID
                    + friend.getId() + " .");
        if (friend.getFriends() == null || !friend.getFriends().contains(user.getId()))
            throw new UserNotFoundException(USER_WITH_THE_ID + friend.getId() + DOESN_T_HAVE_FRIEND_WITH_ID
                    + user.getId() + " .");
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
    }

    public void addFriend(Long userId, Long friendId) {
        var user = storage.getUser(userId);
        if (user == null)
            throw new UserNotFoundException(USER_WITH_THE_ID + userId + DOESN_T_EXIST);
        var friend = storage.getUser(friendId);
        if (friend == null)
            throw new UserNotFoundException(USER_FRIEND_WITH_THE_ID + friendId + DOESN_T_EXIST);
        addFriend(user, friend);
        storage.saveOrUpdate(user);
        storage.saveOrUpdate(friend);
    }

    public void removeFriend(Long userId, Long friendId) {
        var user = storage.getUser(userId);
        if (user == null)
            throw new UserNotFoundException(USER_WITH_THE_ID + userId + DOESN_T_EXIST);
        var friend = storage.getUser(friendId);
        if (friend == null)
            throw new UserNotFoundException(USER_FRIEND_WITH_THE_ID + friendId + DOESN_T_EXIST);
        removeFriend(user, friend);
        storage.saveOrUpdate(user);
        storage.saveOrUpdate(friend);
    }

    public Collection<User> getCommonFriends(Long id, Long otherId) {
        var userFriends = storage.getFriends(id);
        var otherUserFriends = storage.getFriends(otherId);
        return userFriends.stream()
                .distinct()
                .filter(otherUserFriends::contains)
                .collect(Collectors.toSet());
    }

    public void save(User user) {
        this.storage.save(user);
    }

    public void saveOrUpdate(User user) {
        this.storage.saveOrUpdate(user);
    }

    public User getUser(long userId) {
        return this.storage.getUser(userId);
    }

    public Collection<User> getUsers() {
        return this.storage.getUsers();
    }

    public Collection<User> getFriends(long userId) {
        return this.storage.getFriends(userId);
    }
}
