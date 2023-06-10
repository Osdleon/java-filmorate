package ru.yandex.practicum.filmorate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootApplication
public class FilmorateApplication {
    public static void main(String[] args) {
        var limitDate = LocalDate.parse("18951228", DateTimeFormatter.BASIC_ISO_DATE);
        SpringApplication.run(FilmorateApplication.class, args);
    }
}
