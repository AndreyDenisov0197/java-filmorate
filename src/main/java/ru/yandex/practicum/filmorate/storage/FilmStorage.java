package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.List;
import java.util.Set;

public interface FilmStorage {

    Film addFilm(Film film);

    void deleteFilm(Film fim);

    Film updateFilm(Film film);

    List<Film> getFilm();

    List<Film> getPopularFilm(int count);

    Film getFilmByID(Integer id);

    List<Genre> getGenresList();

    Genre getGenreById(int id);

    List<Mpa> getMpaList();

    Mpa getMpaById(int id);

    void addLike(int filmId, int userId);

    void removeLike(int filmId, int userId);

    Set<Genre> getFilmGenresFromDb(int filmId);

    void setFilmGenresToDb(int film, Set<Genre> genreSet);

    List<Film> setGenresToFilmList(List<Film> filmList);
}
