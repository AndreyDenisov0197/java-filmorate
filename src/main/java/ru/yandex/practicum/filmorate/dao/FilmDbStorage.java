package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@RequiredArgsConstructor
@Repository("filmDbStorage")
public class FilmDbStorage implements FilmStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Film addFilm(Film film) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("films")
                .usingGeneratedKeyColumns("id");

        int id = insert.executeAndReturnKey(filmToMap(film)).intValue();
        film.setId(id);
        setFilmGenresToDb(id, film.getGenres());
        film.setGenres(getFilmGenresFromDb(id));
        return film;
    }

    @Override
    public void deleteFilm(Film film) {
        int id = film.getId();
        jdbcTemplate.update("DELETE FROM likes WHERE film_id = ?;", id);
        jdbcTemplate.update("DELETE FROM genre_to_film WHERE film_id = ?;", id);
        jdbcTemplate.update("DELETE FROM films WHERE id = ?;", id);
    }

    @Override
    public Film updateFilm(Film film) {
        int id = film.getId();
        getFilmByID(id);
        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, " +
                "duration = ?, mpa_id = ?" +
                "WHERE id = ?";

        jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().getId(),
                id);
        setFilmGenresToDb(id, film.getGenres());
        film.setGenres(getFilmGenresFromDb(id));
        film.setMpa(getMpaById(film.getMpa().getId()));
        return film;
    }

    @Override
    public List<Film> getFilm() {

        String sql = "SELECT *, mpa.id as mpa_id, mpa.name as mpa_name " +
                "FROM films " +
                "JOIN mpa  ON films.mpa_id = mpa.id " +
                ";";

        List<Film> films = jdbcTemplate.query(sql, getFilmMapper());
        List<Film> filmList = new ArrayList<>();
        for (Film film : films) {
            film.setGenres(getFilmGenresFromDb(film.getId()));
            filmList.add(film);
        }
        return filmList;
    }

    @Override
    public List<Film> getPopularFilm(int count) {

        String sql = "SELECT *, mpa.id as mpa_id, mpa.name as mpa_name, COUNT(likes.user_id) " +
                "FROM films " +
                "JOIN mpa  ON films.mpa_id = mpa.id " +
                "LEFT JOIN likes ON films.id = likes.film_id " +
                "GROUP BY films.id, likes.user_id " +
                "ORDER BY COUNT(likes.user_id) DESC " +
                "LIMIT ?;";

        return jdbcTemplate.query(sql, getFilmMapper(), count);
    }


    @Override
    public Film getFilmByID(Integer id) {
        try {
            Film film = jdbcTemplate.queryForObject(
                    "SELECT *, mpa.id as mpa_id, mpa.name as mpa_name " +
                            "FROM films " +
                            "JOIN mpa  ON films.mpa_id = mpa.id " +
                            "WHERE films.id = ?;", getFilmMapper(), id);
            film.setGenres(getFilmGenresFromDb(film.getId()));
            return film;
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(String.format("Film с ID=%d не существует", id));
        }
    }

    @Override
    public List<Genre> getGenresList() {
        String sql = "SELECT * FROM genres ORDER BY id;";
        return jdbcTemplate.query(sql, getGenreMapper());
    }

    @Override
    public Genre getGenreById(int id) {
        try {
            String sql = "SELECT * FROM genres WHERE id = ?;";
            return jdbcTemplate.queryForObject(sql, getGenreMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(String.format("Жанра с ID=%d не существует", id));
        }
    }

    @Override
    public List<Mpa> getMpaList() {
        String sql = "SELECT * FROM mpa ORDER BY id;";
        return jdbcTemplate.query(sql, getMpaMapper());
    }

    @Override
    public Mpa getMpaById(int id) {
        try {
            String sql = "SELECT * FROM mpa WHERE id = ?;";
            return jdbcTemplate.queryForObject(sql, getMpaMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(String.format("Рейтинга с ID=%d не существует", id));
        }
    }

    @Override
    public void addLike(int filmId, int userId) {
        int insert = jdbcTemplate.update("INSERT INTO likes (film_id, user_id) " +
                "VALUES (?, ?);", filmId, userId);
        if (insert == 0) {
            throw new ObjectNotFoundException("Такого фильма или пользователя не существует!");
        }
    }

    @Override
    public void removeLike(int filmId, int userId) {
        int insert = jdbcTemplate.update("DELETE FROM likes " +
                "WHERE film_id = ? AND user_id = ?;", filmId, userId);
        if (insert == 0) {
            throw new ObjectNotFoundException("Такого фильма или пользователя не существует!");
        }
    }

    @Override
    public Set<Genre> getFilmGenresFromDb(int filmId) {
        return new HashSet<>(jdbcTemplate.query("SELECT * " +
                "FROM genres " +
                "JOIN genre_to_film ON genres.id = genre_to_film.genre_id " +
                "WHERE genre_to_film.film_id = ? " +
                "ORDER BY id;", getGenreMapper(), filmId));
    }


    public void setFilmGenresToDbTest(int filmId, Set<Genre> genreSet) {
        jdbcTemplate.update("DELETE FROM genre_to_film " +
                "WHERE film_id = ?;", filmId);
        if (genreSet == null || genreSet.isEmpty()) {
            return;
        }
        genreSet.forEach(genre -> getGenreById(genre.getId()));
        for (Genre genre : genreSet) {
            jdbcTemplate.update("INSERT INTO genre_to_film (film_id, genre_id) " +
                    "VALUES (?, ?);", filmId, genre.getId());
        }
    }

    @Override
    public void setFilmGenresToDb(int filmId, Set<Genre> genreSet) {
        jdbcTemplate.update("DELETE FROM genre_to_film " +
                "WHERE film_id = ?;", filmId);
        if (genreSet == null || genreSet.isEmpty()) {
            return;
        }
        genreSet.forEach(genre -> getGenreById(genre.getId()));

        List<Genre> genreList = new ArrayList<>(genreSet);
        jdbcTemplate.batchUpdate("INSERT INTO genre_to_film (film_id, genre_id) " +
                "VALUES (?, ?);", new BatchPreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps, int i) throws SQLException {
                ps.setInt(1, filmId);
                ps.setInt(2, genreList.get(i).getId());
            }

            @Override
            public int getBatchSize() {
                return genreList.size();
            }
        });
    }



    private RowMapper<Mpa> getMpaMapper() {
        return ((rs, rowNum) -> new Mpa(rs.getInt("id"), rs.getString("name")));
    }

    private static RowMapper<Genre> getGenreMapper() {
        return (rs, rowNum) -> new Genre(rs.getInt("id"), rs.getString("name"));
    }


    private RowMapper<Film> getFilmMapper() {
        return (rs, rowNum) -> {
            int id = rs.getInt("id");
            Film film = new Film(rs.getString("name"),
                    rs.getString("description"),
                    rs.getDate("release_date").toLocalDate(),
                    rs.getInt("duration"),
                    (new Mpa(rs.getInt("mpa_id"),
                            rs.getString("mpa_name")))
            );
            film.setId(id);
            return film;
        };
    }

    private static Map<String, Object> filmToMap(Film film) {
        Mpa mpa = film.getMpa();
        int id = mpa.getId();
        return Map.of(
                "name", film.getName(),
                "description", film.getDescription(),
                "release_date", Date.valueOf(film.getReleaseDate()),
                "duration", film.getDuration(),
                "mpa_id", id
        );
    }

}

