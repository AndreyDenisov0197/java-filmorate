package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private FilmDbStorage filmDbStorage;
    private Set<Genre> genres;



    @BeforeEach
    public void beforeEach() {
        filmDbStorage = new FilmDbStorage(jdbcTemplate);
        genres = new HashSet<>();
        genres.add(new Genre(2, "Драма"));
        genres.add(new Genre(5, "Документальный"));
    }

    @Test
    public void testFindFilmById() {

        Film film = new Film("Film1", "descriplion",
                LocalDate.of(1990, 1, 1), 144, new Mpa(1), genres);

        Film filmAdd = filmDbStorage.addFilm(film);
        Film film1 = filmDbStorage.getFilmByID(filmAdd.getId());
        filmAdd.setMpa(filmDbStorage.getMpaById(1));

        Assertions.assertEquals(filmAdd.toString(), film1.toString());

        assertThat(film1)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film);
    }

    @Test
    public void testDeleteFilm() {
        Film film = new Film("Film1", "descriplion",
                LocalDate.of(1990, 1, 1), 144, new Mpa(1), genres);

        Film filmAdd = filmDbStorage.addFilm(film);
        Film film1 = filmDbStorage.getFilmByID(filmAdd.getId());
        filmAdd.setMpa(filmDbStorage.getMpaById(1));

        assertThat(film1)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(film);        // и сохраненного пользователя - совпадают

        filmDbStorage.deleteFilm(film1);

        Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            filmDbStorage.getFilmByID(film1.getId());
        });
    }

    @Test
    public void testUpdateFilms() {
        Film film = new Film("Film1", "descriplion",
                LocalDate.of(1990, 1, 1), 144, new Mpa(1), genres);

        Film filmAdd = filmDbStorage.addFilm(film);
        Film film1 = filmDbStorage.getFilmByID(filmAdd.getId());

        film1.setName("Panda");

        filmDbStorage.updateFilm(film1);
        Film film2 = filmDbStorage.getFilmByID(filmAdd.getId());

        assertThat(film2)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(film1);
    }

    @Test
    public void testGetUsers() {
        Film film1 = new Film("Film1", "descriplion",
                LocalDate.of(1990, 1, 1), 144, new Mpa(1), genres);

        Film film2 = new Film("Film1", "descriplion",
                LocalDate.of(1990, 1, 1), 144, new Mpa(1), genres);

        filmDbStorage.addFilm(film1);
        filmDbStorage.addFilm(film2);

        film1.setMpa(filmDbStorage.getMpaById(1));
        film2.setMpa(filmDbStorage.getMpaById(1));

        List<Film> filmList = new ArrayList<>();
        filmList.add(film1);
        filmList.add(film2);

        List<Film> newFilmList = filmDbStorage.getFilm();

        Assertions.assertEquals(filmList.toString(), newFilmList.toString());

        assertThat(newFilmList)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(filmList);
    }

    @Test
    public void testGetGenresList() {
        List<Genre> list1 = new ArrayList<>();

        list1.add(new Genre(1, "Комедия"));
        list1.add(new Genre(2, "Драма"));
        list1.add(new Genre(3, "Мультфильм"));
        list1.add(new Genre(4, "Триллер"));
        list1.add(new Genre(5, "Документальный"));
        list1.add(new Genre(6, "Боевик"));

        List<Genre> list2 = filmDbStorage.getGenresList();

        Assertions.assertEquals(list1.size(), list2.size());
        assertThat(list2).isNotNull();
    }

    @Test
    public void testGetGenreById() {
        Genre genre = new Genre(1, "Комедия");
        Genre genre1 = filmDbStorage.getGenreById(genre.getId());

        Assertions.assertEquals(genre.toString(), genre1.toString());
        assertThat(genre1)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(genre);
    }

    @Test
    public void testGetMpaList() {
        List<Mpa> list1 = new ArrayList<>();
        list1.add(new Mpa(1));
        list1.add(new Mpa(2));
        list1.add(new Mpa(3));
        list1.add(new Mpa(4));
        list1.add(new Mpa(5));

        List<Mpa> list2 = filmDbStorage.getMpaList();

        Assertions.assertEquals(list1.size(), list2.size());
        assertThat(list2).isNotNull();
    }

    @Test
    public void testGetMpaById() {
        Mpa mpa = new Mpa(2, "PG");
        Mpa mpa1 = filmDbStorage.getMpaById(mpa.getId());

        Assertions.assertEquals(mpa.toString(), mpa1.toString());
        assertThat(mpa1)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(mpa);
    }
}
