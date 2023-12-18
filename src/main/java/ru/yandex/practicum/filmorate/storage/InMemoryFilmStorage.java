/*
package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//@Component("InMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    protected final Map<Integer, Film> allFilm = new HashMap<>();
    protected int index = 1;

    @Override
    public List<Film> getFilm() {
        return new ArrayList<>(allFilm.values());
    }

    @Override
    public void deleteFilm(Film film) {
        Integer id = film.getId();
        if (allFilm.get(id) == film) {
            allFilm.remove(id);
        } else {
            throw new ObjectNotFoundException("Фильм не существует");
        }
    }

    @Override
    public Film addFilm(Film film) {
        if (null == film.getId()) {
            while (allFilm.containsKey(index)) {
                ++index;
            }
            film.setId(index);
        }
        int id = film.getId();
        allFilm.put(id, film);
        return allFilm.get(id);
    }

    @Override
    public Film updateFilm(Film film) {
        int id = film.getId();
        if (allFilm.containsKey(id)) {
            film.setId(id);
            allFilm.put(id, film);
        } else {
            throw new ObjectNotFoundException("Фильм не существует");
        }
        return allFilm.get(film.getId());
    }

    @Override
    public Film getFilmByID(Integer id) {
        Film film = allFilm.get(id);
        if (film == null) {
            throw new ObjectNotFoundException(String.format("Фильма с ID=%d не существует", id));
        }
        return film;
    }

    @Override
    public List<Genre> getGenresList() {
        return null;
    }

    @Override
    public Genre getGenreById(int id) {
        return null;
    }

    @Override
    public List<Mpa> getMpaList() {
        return null;
    }

    @Override
    public Mpa getMpaById(int id) {
        return null;
    }
}
*/
