package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;

    public void addLike(int id, int userId) {
        Film film = filmStorage.getFilmByID(id);
        if (film == null) {
            throw new ObjectNotFoundException(String.format("Фильма с ID=%d не существует", id));
        }
        film.addLike(userId);
    }

    public void removeLike(int id, int userId) {
        Film film = filmStorage.getFilmByID(id);
        if (film == null) {
            throw new ObjectNotFoundException(String.format("Фильма с ID=%d не существует", id));
        }
        film.removeLike(userId);
    }

    public List<Film> getTop10Films(int n) {
        List<Film> films = filmStorage.getFilm();
        Collections.sort(films);
        ArrayList<Film> topFilm = new ArrayList<>();

        for (int i = 0; i < (n); i++) {
            topFilm.add(films.get(i));
        }

        return topFilm;
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
