package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

public class FilmControllerTest {

    FilmController filmController;

    @BeforeEach
    void beforeEach() {
        filmController = new FilmController();
    }

    @Test
    void checkFilm() {
        Film film = Film.builder()
                .description("description of film")
                .releaseDate(LocalDate.parse("1997-01-15"))
                .duration(190)
                .build();

        film.setName("name"); // почему билдер не видет родительский класс????

        Film postFilm = filmController.addFile(film);
        film.setId(1);
        Assertions.assertEquals(film, postFilm, "User не совпвдвет");
    }

    @Test
    void checkNameNull() {
        Film film = Film.builder()
                .description("description of film")
                .releaseDate(LocalDate.parse("1997-01-15"))
                .duration(150)
                .build();

        film.setName(null);

        Assertions.assertThrows(ValidationException.class, () -> filmController.addFile(film),
                "Ошибка Name == null.");
    }

    @Test
    void checkNameBlank() {
        Film film = Film.builder()
                .description("description of film")
                .releaseDate(LocalDate.parse("1997-01-15"))
                .duration(150)
                .build();

        film.setName("");

        Assertions.assertThrows(ValidationException.class, () -> filmController.addFile(film),
                "Ошибка пустое поле Name.");
    }

    @Test
    void checkNameBlank2() {
        Film film = Film.builder()
                .description("description of film")
                .releaseDate(LocalDate.parse("1997-01-15"))
                .duration(150)
                .build();

        film.setName("   ");

        Assertions.assertThrows(ValidationException.class, () -> filmController.addFile(film),
                "Ошибка пустое поле Name.");
    }

    @Test
    void checkDescription455() {
        Film film = Film.builder()
                .description("What the movie is about.\n" +
                        "In Spain, on an orange plantation, farmers are annoyed by a furry beast (actually Cheburashka" +
                        " – but he will take this name only at the end of the film). He loves oranges and devours " +
                        "dozens of them. The patience of the farmers comes to an end, and they decide to catch the " +
                        "animal with a trap. The idea was almost a success, but suddenly the weather deteriorates and" +
                        " a tornado takes away the harvest of oranges along with Cheburashka.")
                .releaseDate(LocalDate.parse("2023-01-15"))
                .duration(150)
                .build();

        film.setName("Cheburashka");

        Assertions.assertThrows(ValidationException.class, () -> filmController.addFile(film),
                "Ошибка описание слишком длинное.");
    }

    @Test
    void checkDateRelease() {
        Film film = Film.builder()
                .description("description of film")
                .releaseDate(LocalDate.parse("1895-12-25"))
                .duration(150)
                .build();

        film.setName("name");

        Assertions.assertThrows(ValidationException.class, () -> filmController.addFile(film),
                "Ошибка Дата релиза раньше с порогового значения.");
    }


    @Test
    void checkDuration0() {
        Film film = Film.builder()
                .description("description of film")
                .releaseDate(LocalDate.parse("1995-12-25"))
                .duration(0)
                .build();

        film.setName("name");

        Assertions.assertThrows(ValidationException.class, () -> filmController.addFile(film),
                "Ошибка продолжительность = 0.");
    }

    @Test
    void checkDurationMinus() {
        Film film = Film.builder()
                .description("description of film")
                .releaseDate(LocalDate.parse("1995-12-25"))
                .duration(-30)
                .build();

        film.setName("name");

        Assertions.assertThrows(ValidationException.class, () -> filmController.addFile(film),
                "Ошибка отрицательная продолжительность.");
    }

    @Test
    void checkDurationNull() {
        Film film = Film.builder()
                .description("description of film")
                .releaseDate(LocalDate.parse("1995-12-25"))
                .duration(null)
                .build();

        film.setName("name");

        Assertions.assertThrows(ValidationException.class, () -> filmController.addFile(film),
                "Ошибка отрицательная продолжительность.");
    }

    @Test
    void checkDurationNull1() {
        Film film = Film.builder()
                .description("description of film")
                .releaseDate(LocalDate.parse("1995-12-25"))
                .build();

        film.setName("name");

        Assertions.assertThrows(ValidationException.class, () -> filmController.addFile(film),
                "Ошибка отрицательная продолжительность.");
    }
}
