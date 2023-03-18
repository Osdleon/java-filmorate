package ru.yandex.practicum.filmorate.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UsersRepository;

import javax.validation.Valid;
import java.util.Collection;


@RestController
@RequestMapping("users")
@Validated
public class UserController {
    UsersRepository repository;

    public UserController() {
        repository = new UsersRepository();
    }

    @PostMapping
    User saveUser(@RequestBody @Valid User user) {
        repository.save(user);
        return user;
    }

    @PutMapping
    User updateUser(@RequestBody @Valid User user) {
        repository.saveOrUpdate(user);
        return user;
    }

    @GetMapping
    Collection<User> getUsers() {
        return repository.getUsers();
    }
}
