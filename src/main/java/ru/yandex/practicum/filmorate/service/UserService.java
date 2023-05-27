package ru.yandex.practicum.filmorate.service;

import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.interfaces.UserStorage;

import java.util.Collection;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class UserService {
    void addFriend(User user, User friend) {
        if (user.getFriends() == null)
            user.setFriends(new HashSet<>());
        else if (user.getFriends().contains(friend.getId()))
            throw new UserNotFoundException("User with the id: " + user.getId() + "already have friend with id:"
                    + friend.getId() + " .");
        if (friend.getFriends() == null)
            friend.setFriends(new HashSet<>());
        else if (friend.getFriends().contains(user.getId()))
            throw new UserNotFoundException("User with the id: " + friend.getId() + "already have friend with id:"
                    + user.getId() + " .");
        user.getFriends().add(friend.getId());
        friend.getFriends().add(user.getId());
    }

    void removeFriend(User user, User friend) {
        if (user.getFriends() == null || !user.getFriends().contains(friend.getId()))
            throw new UserNotFoundException("User with the id: " + user.getId() + "doesn't have friend with id:"
                    + friend.getId() + " .");
        if (friend.getFriends() == null || !friend.getFriends().contains(user.getId()))
            throw new UserNotFoundException("User with the id: " + friend.getId() + "doesn't have friend with id:"
                    + user.getId() + " .");
        user.getFriends().remove(friend.getId());
        friend.getFriends().remove(user.getId());
    }

    public void addFriend(Long userId, Long friendId, UserStorage userStorage) {
        var user = userStorage.getUser(userId);
        if (user == null)
            throw new UserNotFoundException("User with the id: " + userId + "doesn't exist.");
        var friend = userStorage.getUser(friendId);
        if (friend == null)
            throw new UserNotFoundException("User friend with the id: " + friendId + "doesn't exist.");
        addFriend(user, friend);
        userStorage.saveOrUpdate(user);
        userStorage.saveOrUpdate(friend);
    }

    public void removeFriend(Long userId, Long friendId, UserStorage userStorage) {
        var user = userStorage.getUser(userId);
        if (user == null)
            throw new UserNotFoundException("User with the id: " + userId + "doesn't exist.");
        var friend = userStorage.getUser(friendId);
        if (friend == null)
            throw new UserNotFoundException("User friend with the id: " + friendId + "doesn't exist.");
        removeFriend(user, friend);
        userStorage.saveOrUpdate(user);
        userStorage.saveOrUpdate(friend);
    }

    public Collection<User> getCommonFriends(Long id, Long otherId, UserStorage userStorage) {
        var userFriends = userStorage.getFriends(id);
        var otherUserFriends = userStorage.getFriends(otherId);
        return userFriends.stream()
                .distinct()
                .filter(otherUserFriends::contains)
                .collect(Collectors.toSet());
    }
}
