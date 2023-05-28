package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode
public class User {
    @EqualsAndHashCode.Include
    long id;
    @NotBlank @Email String email;
    @NotBlank String login;
    String name;
    @Past LocalDate birthday;
    Set<Long> friends;

    public User() {
        friends = new HashSet<>();
    }
}
