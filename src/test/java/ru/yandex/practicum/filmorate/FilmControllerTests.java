package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.util.NestedServletException;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exceptions.FilmValidationException;

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
        mockMvc = MockMvcBuilders.standaloneSetup(new FilmController()).build();
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
                .andExpect(status().is(400))
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
                .andExpect(status().is(400))
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
                .andExpect(status().is(400))
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void postFilmInvalidDate() throws Exception {
        var e = Assertions.assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(post("/films")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                            "  \"name\": \"nisi eiusmod\",\n" +
                            "  \"description\": \"adipisicing\",\n" +
                            "  \"releaseDate\": \"1767-03-25\",\n" +
                            "  \"duration\": 100\n" +
                            "}"));
        });
        assertTrue(e.getCause() instanceof FilmValidationException);
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
                .andExpect(status().is(400))
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
                .andExpect(status().is(400))
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
                .andExpect(status().is(400))
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void updateFilm() throws Exception {
        var e = Assertions.assertThrows(NestedServletException.class, () -> {
        mockMvc.perform(put("/films")
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
        });
        assertTrue(e.getCause() instanceof FilmValidationException);
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
                .andExpect(status().is(400))
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof MethodArgumentNotValidException));
    }

    @Test
    void updateFilmInvalidDate() throws Exception {
        var e = Assertions.assertThrows(NestedServletException.class, () -> {
            mockMvc.perform(put("/films")
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
                    .content("{\n" +
                            "  \"name\": \"nisi eiusmod\",\n" +
                            "  \"description\": \"adipisicing\",\n" +
                            "  \"releaseDate\": \"1767-03-25\",\n" +
                            "  \"duration\": 100\n" +
                            "}"));
        });
        assertTrue(e.getCause() instanceof FilmValidationException);
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
                .andExpect(status().is(400))
                .andExpect(r -> assertTrue(r.getResolvedException() instanceof MethodArgumentNotValidException));
    }
}