package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.model.UsersRepository;

import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class UserTests {

    private static Validator validator;

    static {
        var validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.usingContext().getValidator();
    }
    User createTestUser(){
        var user =  new User();
        user.setId(1);
        user.setLogin("login");
        user.setName("name");
        user.setEmail("a@b.ru");
        user.setBirthday(LocalDate.parse("20230101", DateTimeFormatter.BASIC_ISO_DATE));
        return user;
    }
    @Test
    void validateEmptyLogin() {
        var user = createTestUser();
        user.setLogin(" ");
        var violations = validator.validate(user);
        Assertions.assertEquals(1, violations.size());
    }
    @Test
    void validateLogin() {
        var user = createTestUser();
        user.setLogin("1");
        var violations = validator.validate(user);
        Assertions.assertEquals(0, violations.size());
    }
    @Test
    void validateEmptyName() {
        var user = createTestUser();
        user.setName("");
        var r = new UsersRepository();
        r.save(user);
        Assertions.assertEquals(user.getLogin(), user.getName());
        var violations = validator.validate(user);
        Assertions.assertEquals(0, violations.size());
    }

    @Test
    void validateName() {
        var user = createTestUser();
        user.setName("a");
        var violations = validator.validate(user);
        Assertions.assertEquals(0, violations.size());
    }
    @Test
    void validateEmail() {
        var user = createTestUser();
        user.setEmail("a");
        var violations = validator.validate(user);
        Assertions.assertEquals(1, violations.size());
    }
}
