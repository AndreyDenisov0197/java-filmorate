package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @GetMapping("/films")
    public List<Film> getFilm() {
        log.info("GET /films");
        return filmService.getFilm();
    }

    @PostMapping("/films")
    public Film addFilm(@RequestBody Film film) {
        log.info("POST /films");
        validate(film);
        return filmService.addFilm(film);
    }

    @GetMapping("/films/{id}")
    public Film getFilmById(@PathVariable int id) {
        log.info("GET /films/{}", id);
        return filmService.getFilmByID(id);
    }

    @PutMapping("/films")
    public Film updateFilm(@RequestBody Film film) {
        log.info("PUT /films");
        validate(film);
        return filmService.updateFilm(film);
    }

    @PutMapping("/films/{id}/like/{userId}")
    public void addLike(@PathVariable int id, @PathVariable int userId) {
        log.info("PUT /films/{}/like/{}", id, userId);
        filmService.addLike(id, userId);
    }

    @DeleteMapping("/films/{id}/like/{userId}")
    public void removeLike(@PathVariable int id, @PathVariable int userId) {
        log.info("DELETE /films/{}/like/{}", id, userId);
        filmService.removeLike(id, userId);
    }

    @GetMapping("/films/popular")
    public List<Film> getTop10Films(@RequestParam(required = false) Integer count) {
        if (count == null) {
            count = 10;
        }
        return filmService.getTop10Films(count);
    }

    @GetMapping("/genres")
    public List<Genre> getGenresList() {
        return filmService.getGenresList();
    }

    @GetMapping("/genres/{id}")
    public Genre getGenreById(@PathVariable int id) {
        return filmService.getGenreById(id);
    }

    @GetMapping("/mpa")
    public List<Mpa> getMpaList() {
        return filmService.getMpaList();
    }

    @GetMapping("/mpa/{id}")
    public Mpa getMpaList(@PathVariable int id) {
        return filmService.getMpaById(id);
    }



    protected void validate(Film film) {
        String name = film.getName();
        if (name == null || name.isBlank()) {
            log.error("Название фильма пустое.");
            throw new ValidationException("Название фильма пустое.");
        }

        if (film.getDescription().length() > 200) {
            log.error("Описание фильма больше 200 символов.");
            throw new ValidationException("Описание фильма больше 200 символов.");
        }

        LocalDate date = LocalDate.of(1895,12,28);
        if (date.isAfter(film.getReleaseDate())) {
            log.error("Дата релиза раньше 28 декабря 1895 года.");
            throw new ValidationException("Дата релиза раньше 28 декабря 1895 года.");
        }

        Integer duration = film.getDuration();
        if (duration == null || duration <= 0) {
            log.error("Продолжительность фильма отрицательная или равна нулю.");
            throw new ValidationException("Продолжительность фильма отрицательная или равна нулю.");
        }
    }
}
