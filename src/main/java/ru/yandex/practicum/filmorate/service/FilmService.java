package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserService userService;
    public static final Comparator<Film> FILM_COMPARATOR = Comparator.comparingLong(Film::getRate).reversed();

    public void addLike(int id, int userId) {
        Film film = filmStorage.getFilmByID(id);
        if (film == null) {
            throw new ObjectNotFoundException(String.format("Фильма с ID=%d не существует", id));
        }

        if (userService.getUserByID(userId) == null) {
            throw new ObjectNotFoundException(String.format("Пользователя с ID=%d не существует", userId));
        }
        film.addLike(userId);
    }

    public void removeLike(int id, int userId) {
        Film film = filmStorage.getFilmByID(id);
        if (film == null) {
            throw new ObjectNotFoundException(String.format("Фильма с ID=%d не существует", id));
        }
        if (userService.getUserByID(userId) == null) {
            throw new ObjectNotFoundException(String.format("Пользователя с ID=%d не существует", userId));
        }

        film.removeLike(userId);
    }

    public List<Film> getTop10Films(int count) {
        return filmStorage.getFilm().stream()
                .sorted(FILM_COMPARATOR)
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Film> getFilm() {
        return filmStorage.getFilm();
    }

    public void deleteFilm(Film film) {
        filmStorage.deleteFilm(film);
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film film) {
        return filmStorage.updateFilm(film);
    }

    public Film getFilmByID(Integer id) {
        return filmStorage.getFilmByID(id);
    }
}
