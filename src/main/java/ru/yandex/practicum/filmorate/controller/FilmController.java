package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
public class FilmController extends Controller<Film> {

    @Override
    @GetMapping("/films")
    public List<Film> getFile() {
        return super.getFile();
    }

    @Override
    @PostMapping("/films")
    public Film addFile(@RequestBody Film file) {
        return super.addFile(file);
    }

    @Override
    @PutMapping("/films")
    public Film updateFile(@RequestBody Film file) {
        return super.updateFile(file);
    }

    @Override
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
