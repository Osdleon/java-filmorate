package ru.yandex.practicum.filmorate.model;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Data
public class Film {
    long id;
    @NotBlank
    String name;
    @Size(max = 200)
    String description;
    LocalDate releaseDate;
    @Positive
    long duration;
    Set<Long> likes;
    Set<Genre> genres;
    Mpa mpa;

    public Film() {
        genres = new HashSet<>();
        likes = new HashSet<>();
    }
}
