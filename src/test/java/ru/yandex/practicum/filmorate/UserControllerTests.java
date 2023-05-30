package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
class UserControllerTests {
    private MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        mockMvc = MockMvcBuilders.standaloneSetup(
                new UserController(
                        new UserService(new InMemoryUserStorage()))).build();
    }

    @Test
    void getEmptyUsers() throws Exception {
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void postUser() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(r -> assertNull(r.getResolvedException()));
    }

    @Test
    void postUserWithEmptyName() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(r -> assertNull(r.getResolvedException()))
                .andExpect(content().string("{\"id\":1,\"email\":\"mail@mail.ru\"," +
                        "\"login\":\"dolore\",\"name\":\"dolore\",\"birthday\":[1946,8,20],\"friends\":[]}"));
    }

    @Test
    void postUserWithoutEmail() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}"))
                .andExpect(status().is(500))
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void postUserWithIncorrectEmail() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"eeee\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}"))
                .andExpect(status().is(500))
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void postUserWithEmptyLogin() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}"))
                .andExpect(status().is(500))
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void postUserWithBlankLogin() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}"))
                .andExpect(status().is(500))
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void postUserWithFutureBirthday() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"7946-08-20\"\n" +
                                "}"))
                .andExpect(status().is(500))
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof MethodArgumentNotValidException));
    }


    @Test
    void updateUser() throws Exception {
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}"))
                .andExpect(status().isNotFound())
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof UserNotFoundException));
    }

    @Test
    void updateUserWithoutEmail() throws Exception {
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}"))
                .andExpect(status().is(500))
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void updateUserWithIncorrectEmail() throws Exception {
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"eeee\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}"))
                .andExpect(status().is(500))
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void updateUserWithEmptyLogin() throws Exception {
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}"))
                .andExpect(status().is(500))
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void updateUserWithBlankLogin() throws Exception {
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}"))
                .andExpect(status().is(500))
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void updateUserWithFutureBirthday() throws Exception {
        mockMvc.perform(put("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"7946-08-20\"\n" +
                                "}"))
                .andExpect(status().is(500))
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void addInvalidFriend() throws Exception {
        mockMvc.perform(put("/users/1/friends/2"))
                .andExpect(status().isNotFound())
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof UserNotFoundException));
    }

    @Test
    void deleteInvalidFriend() throws Exception {
        mockMvc.perform(delete("/users/1/friends/2"))
                .andExpect(status().isNotFound())
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof UserNotFoundException));
    }

    @Test
    void getInvalidFriends() throws Exception {
        mockMvc.perform(get("/users/1/friends"))
                .andExpect(status().isNotFound())
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof UserNotFoundException));
    }

    @Test
    void getInvalidCommonFriends() throws Exception {
        mockMvc.perform(get("/users/1/friends/common/2"))
                .andExpect(status().isNotFound())
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof UserNotFoundException));
    }

    @Test
    void addFriend() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"login\": \"dolore\",\n" +
                        "  \"name\": \"Test Name\",\n" +
                        "  \"email\": \"mail@mail.ru\",\n" +
                        "  \"birthday\": \"1946-08-20\"\n" +
                        "}"));
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"login\": \"dolore\",\n" +
                        "  \"name\": \"Nick Name\",\n" +
                        "  \"email\": \"mail@mail.ru\",\n" +
                        "  \"birthday\": \"1946-08-20\"\n" +
                        "}"));

        mockMvc.perform(put("/users/1/friends/2"));

        mockMvc.perform(get("/users/1/friends"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "[{\"id\":2,\"email\":\"mail@mail.ru\",\"login\":\"dolore\"," +
                                "\"name\":\"Nick Name\",\"birthday\":[1946,8,20],\"friends\":[1]}]"
                ));
    }

    @Test
    void geCommonFriend() throws Exception {
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"login\": \"dolore\",\n" +
                        "  \"name\": \"Test Name\",\n" +
                        "  \"email\": \"mail@mail.ru\",\n" +
                        "  \"birthday\": \"1946-08-20\"\n" +
                        "}"));
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"login\": \"dolore\",\n" +
                        "  \"name\": \"Nick Name\",\n" +
                        "  \"email\": \"mail@mail.ru\",\n" +
                        "  \"birthday\": \"1946-08-20\"\n" +
                        "}"));
        mockMvc.perform(post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\n" +
                        "  \"login\": \"dolore\",\n" +
                        "  \"name\": \"Common Friend\",\n" +
                        "  \"email\": \"mail@mail.ru\",\n" +
                        "  \"birthday\": \"1946-08-20\"\n" +
                        "}"));

        mockMvc.perform(put("/users/1/friends/3"));
        mockMvc.perform(put("/users/2/friends/3"));

        mockMvc.perform(get("/users/1/friends/common/2"))
                .andExpect(status().isOk())
                .andExpect(content().string(
                        "[{\"id\":3,\"email\":\"mail@mail.ru\",\"login\":\"dolore\"" +
                                ",\"name\":\"Common Friend\",\"birthday\":[1946,8,20],\"friends\":[1,2]}]"
                ));
    }
}