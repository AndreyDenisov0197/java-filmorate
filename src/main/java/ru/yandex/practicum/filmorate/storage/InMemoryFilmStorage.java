package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Component
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
        if (film.getId() == null) {
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
}
