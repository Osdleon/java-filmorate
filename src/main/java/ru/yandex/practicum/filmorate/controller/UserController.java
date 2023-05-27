package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;


@RestController
@RequestMapping("users")
@Validated
public class UserController {
    InMemoryUserStorage storage;
    UserService userService;

    @Autowired
    public UserController(InMemoryUserStorage storage, UserService userService) {

        this.storage = storage;
        this.userService = userService;
    }

    @PostMapping
    User saveUser(@RequestBody @Valid User user) {
        storage.save(user);
        return user;
    }

    @PutMapping
    User updateUser(@RequestBody @Valid User user) {
        storage.saveOrUpdate(user);
        return user;
    }

    @GetMapping
    Collection<User> getUsers() {
        return storage.getUsers();
    }

    @PutMapping("/{id}/friends/{friendId}")
    void addFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.addFriend(id, friendId, storage);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    void deleteFriend(@PathVariable long id, @PathVariable long friendId) {
        userService.removeFriend(id, friendId, storage);
    }

    @GetMapping("/{id}/friends")
    Collection<User> getFriends(@PathVariable long id) {
        return storage.getFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    Collection<User> getCommonFriends(@PathVariable long id, @PathVariable long otherId) {
        return userService.getCommonFriends(id, otherId, storage);
    }

    @GetMapping("/{id}")
    User getUser(@PathVariable long id) {
        return storage.getUser(id);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleUserValidation(final UserValidationException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleUserValidation(final UserNotFoundException e) {
        return Map.of("error", e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> handleUserException(final Exception e) {
        return Map.of("error", e.getMessage());
    }
}
