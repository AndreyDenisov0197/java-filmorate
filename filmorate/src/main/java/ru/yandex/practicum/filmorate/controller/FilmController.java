package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class FilmController {
    private final Map<Integer, Film> allFilms = new HashMap<>();
    private int id = 0;

    @GetMapping("/film")
    public List<Film> getFilms() {
        return new ArrayList<>(allFilms.values());
    }

    @PostMapping("/film")
    public Film addFilm(@RequestBody  Film film) {
        validate(film);
        film.setId(++id);
        int id = film.getId();
        allFilms.put(id, film);
        log.info("Фильм {} добавлен", film.getName());
        return allFilms.get(id);
    }

    @PutMapping("/film/{id}")
    public Film updateFilm(@RequestBody Film film) {
        validate(film);
        if (allFilms.containsKey(id)) {
            film.setId(id);
            allFilms.put(id, film);
            log.info("Обновили фильм: {}", film.getName());
        } else {
            log.warn("Обновление невозможно. Фильм {} не добавлен в коллекцию", film.getName());
        }
        return allFilms.get(film.getId());
    }

    private void validate(Film film) {
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
        if (date.isAfter(film.getReleaseDate())){
            log.error("Дата релиза раньше 28 декабря 1895 года.");
            throw new ValidationException("Дата релиза раньше 28 декабря 1895 года.");
        }

        if (film.getDuration() <= 0) {
            log.error("Продолжительность фильма отрицательная или равна нулю.");
            throw new ValidationException("Продолжительность фильма отрицательная или равна нулю.");
        }
    }
}
