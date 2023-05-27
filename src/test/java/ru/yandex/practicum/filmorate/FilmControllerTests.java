package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.service.ValidationService;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class FilmControllerTests {
    private MockMvc mockMvc;

    @BeforeEach
    void setup(WebApplicationContext wac) {
        mockMvc = MockMvcBuilders.standaloneSetup(
                new FilmController(
                        new InMemoryFilmStorage(),
                        new ValidationService(),
                        new FilmService()),
                new UserController(
                        new InMemoryUserStorage(),
                        new UserService())).build();
    }

    @Test
    void getEmptyFilms() throws Exception {
        mockMvc.perform(get("/films"))
                .andExpect(status().isOk())
                .andExpect(content().string("[]"));
    }

    @Test
    void postFilmWithoutName() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": 100\n" +
                                "}"))
                .andExpect(status().is(500))
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void postFilmEmptyName() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": 100\n" +
                                "}"))
                .andExpect(status().is(500))
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void postFilm() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": 100\n" +
                                "}"))
                .andExpect(status().isOk())
                .andExpect(r -> assertNull(r.getResolvedException()));
    }

    @Test
    void postFilmNegativeDuration() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"a\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": -1\n" +
                                "}"))
                .andExpect(status().is(500))
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void postFilmInvalidDate() throws Exception {
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1767-03-25\",\n" +
                                "  \"duration\": 100\n" +
                                "}"))
                .andExpect(status().isBadRequest())
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof FilmValidationException));
    }

    @Test
    void postFilmLongDescription() throws Exception {
        var longStr = FilmTests.getLongString(201);
        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod\",\n" +
                                "  \"description\": \"" + longStr + "\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": 100\n" +
                                "}"))
                .andExpect(status().is(500))
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void updateFilmWithoutName() throws Exception {
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": 100\n" +
                                "}"))
                .andExpect(status().is(500))
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void updateFilmEmptyName() throws Exception {
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": 100\n" +
                                "}"))
                .andExpect(status().is(500))
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void updateNotExistedFilm() throws Exception {
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": 100\n" +
                                "}"))
                .andExpect(status().isNotFound())
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof FilmNotFoundException));
    }

    @Test
    void updateFilmNegativeDuration() throws Exception {
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"a\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": -1\n" +
                                "}"))
                .andExpect(status().is(500))
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void updateFilmInvalidDate() throws Exception {
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1767-03-25\",\n" +
                                "  \"duration\": 100\n" +
                                "}"))
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof FilmValidationException));
    }

    @Test
    void updateFilmLongDescription() throws Exception {
        var longStr = FilmTests.getLongString(201);
        mockMvc.perform(put("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod\",\n" +
                                "  \"description\": \"" + longStr + "\",\n" +
                                "  \"releaseDate\": \"1967-03-25\",\n" +
                                "  \"duration\": 100\n" +
                                "}"))
                .andExpect(status().is(500))
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void addInvalidLike() throws Exception {
        mockMvc.perform(put("/films/1/like/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteInvalidLike() throws Exception {
        mockMvc.perform(delete("/films/1/like/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getPopularFilms() throws Exception {
        mockMvc.perform(get("/films/popular?count=11"))
                .andExpect(status().isOk());
    }

    @Test
    void addLike() throws Exception {

        mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"nisi eiusmod\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1987-03-25\",\n" +
                                "  \"duration\": 100\n" +
                                "}"))
                .andDo(r -> mockMvc.perform(post("/films")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"name\": \"Test Film\",\n" +
                                "  \"description\": \"adipisicing\",\n" +
                                "  \"releaseDate\": \"1987-03-25\",\n" +
                                "  \"duration\": 100\n" +
                                "}")))
                .andDo(r -> mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\n" +
                                "  \"login\": \"dolore\",\n" +
                                "  \"name\": \"Nick Name\",\n" +
                                "  \"email\": \"mail@mail.ru\",\n" +
                                "  \"birthday\": \"1946-08-20\"\n" +
                                "}")))
                .andDo(r -> mockMvc.perform(put("/films/2/like/1")));

        mockMvc.perform(get("/films/popular?count=3"))
                .andExpect(status().isOk())
                .andExpect(content().json(
                        "[{\"id\":2,\"name\":\"Test Film\",\"description\":\"adipisicing\",\"releaseDate\":[1987,3,25]" +
                                ",\"duration\":100,\"likes\":[1]},{\"id\":1,\"name\":\"nisi eiusmod\",\"description\":\"adipisicing\"" +
                                ",\"releaseDate\":[1987,3,25],\"duration\":100,\"likes\":[]}]"));
    }
}