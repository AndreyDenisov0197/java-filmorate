package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;

import java.util.List;

public interface FilmStorage {
    Film addFilm(Film film);
    void deleteFilm(Film fim);
    Film updateFilm(Film film);
    List<Film> getFilm();
    Film getFilmByID(Integer id);

}
