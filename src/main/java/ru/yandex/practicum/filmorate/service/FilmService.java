package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Qualifier("filmDbStorage")
public class FilmService {
    private final FilmStorage filmStorage;

    public void addLike(int id, int userId) {
        filmStorage.addLike(id, userId);
    }

    public void removeLike(int id, int userId) {
        filmStorage.removeLike(id, userId);
    }

    public List<Film> getTop10Films(int count) {
        return filmStorage.getFilm().stream()
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

    public List<Genre> getGenresList() {
        return filmStorage.getGenresList();
    }

    public Genre getGenreById(int id) {
        return filmStorage.getGenreById(id);
    }

    public List<Mpa> getMpaList() {
        return filmStorage.getMpaList();
    }

    public Mpa getMpaById(int id) {
        return filmStorage.getMpaById(id);
    }
}
