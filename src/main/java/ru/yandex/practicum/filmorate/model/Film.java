package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@Data
public class Film {
    int id;
    @NotBlank
    String name;
    @Size(max = 200)
    String description;
    LocalDate releaseDate;
    @Positive
    long duration;
}
