package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.Date;
import java.time.LocalDate;
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
        updateGenresAndRating(film);
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

        String sqlQuery = "UPDATE films SET " +
                "name = ?, description = ?, release_date = ?, " +
                "duration = ?, rating_id = ?" +
                "WHERE id = ?";

        int result = jdbcTemplate.update(sqlQuery,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRating().getId(),
                id);

        if (result == 1) {
            jdbcTemplate.update("DELETE FROM genre_to_film WHERE film_id = ?;", id);
            jdbcTemplate.update("DELETE FROM likes WHERE film_id = ?;", id);
            updateGenresAndRating(film);
        }
        return film;
    }

    @Override
    public List<Film> getFilm() {
        List<Film> films = new ArrayList<>();
        String sql = "SELECT f.id as id, f.name as name, f.description as description, " +
                "f.release_date AS release_date, f.duration as duration, " +
                "f.rating_id as rating_id, r.name as rating_name " +
                "FROM films AS f " +
                "LEFT JOIN rating AS r ON f.rating_id = r.id;";
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
        list.forEach(rs -> {
            Date date = (Date) rs.get("release_date");
            LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();

                    Film film = new Film(
                            (String) rs.get("name"),
                            (String) rs.get("description"),
                            localDate,
                            (Integer) rs.get("duration"),
                            new Mpa((Integer) rs.get("rating_id"),
                                    (String) rs.get("rating_name"))
                    );
                    int id = (Integer) rs.get("id");
                    film.setId(id);
                    getGenresAndLikes(film);
                    films.add(film);
                });
        return films;
    }

    @Override
    public Film getFilmByID(Integer id) {
        Film film = jdbcTemplate.queryForObject(
                "SELECT f.id AS id, f.name AS name, f.description AS description, " +
                        "f.release_date AS release_date, f.duration AS duration, " +
                        "f.rating_id AS rating_id, r.name AS rating_name " +
                        "FROM films AS f " +
                        "LEFT JOIN rating AS r ON f.rating_id = r.id " +
                        "WHERE f.id = ?;", getFilmMapper(), id);
        if (film != null) {
            getGenresAndLikes(film);
            return film;
        }
        return null;
    }

    @Override
    public List<Genre> getGenresList() {
        String sql = "SELECT * FROM genres;";
        List<Genre> genres = new ArrayList<>();

        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
        list.forEach(rs -> {
            Genre genre = new Genre(
                    (Integer) rs.get("id"),
                    (String) rs.get("name")
            );
            genres.add(genre);
        });
        return genres;
    }

    @Override
    public Genre getGenreById(int id) {
        String sql = "SELECT * FROM genres WHERE id = ?;";
        return jdbcTemplate.queryForObject(sql, getGenreMapper(), id);
    }

    @Override
    public List<Mpa> getMpaList() {
        String sql = "SELECT * FROM rating;";
        List<Mpa> mpas = new ArrayList<>();

        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
        list.forEach(rs -> {
            Mpa mpa = new Mpa(
                    (Integer) rs.get("id"),
                    (String) rs.get("name")
            );
            mpas.add(mpa);
        });
        return mpas;
    }

    @Override
    public Mpa getMpaById(int id) {
        String sql = "SELECT * FROM rating WHERE id = ?;";
        return jdbcTemplate.queryForObject(sql, getMpaMapper(), id);
    }

    private RowMapper<Mpa> getMpaMapper() {
        return ((rs, rowNum) -> new Mpa(rs.getInt("id"), rs.getString("name")));
    }

    private static RowMapper<Genre> getGenreMapper() {
        return (rs, rowNum) -> new Genre(rs.getInt("id"), rs.getString("name"));
    }


    private void getGenresAndLikes(Film film) {
        int id = film.getId();
        List<Integer> like = jdbcTemplate.queryForList(
                "SELECT user_id FROM likes WHERE film_id = ?;", Integer.class, id);

        String sql = "SELECT gf.genre_id, g.name " +
                "FROM genre_to_film AS gf " +
                "LEFT JOIN genres AS g ON gf.genre_id = g.id " +
                "WHERE gf.film_id = ?;";

        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql, id);
        List<Genre> genres = new ArrayList<>();
        list.forEach(m -> {
            Genre genre = new Genre((Integer)m.get("genre_id"), (String)m.get("name"));
            genres.add(genre);
        });
        film.setLike(new HashSet<>(like));
        film.setGenre(new HashSet<>(genres));
    }

    private static RowMapper<Film> getFilmMapper() {
        return (rs, rowNum) -> {
            Film film = new Film(rs.getString("name"),
                    rs.getString("description"),
                    rs.getDate("release_date").toLocalDate(),
                    rs.getInt("duration"),
                    (new Mpa(rs.getInt("rating_id"),
                            rs.getString("rating_name")))
            );
            film.setId(rs.getInt("id"));
            return film;
        };
    }

    private static Map<String, Object> filmToMap(Film film) {
        Mpa mpa = film.getRating();
        int id = mpa.getId();
        return Map.of(
                "name", film.getName(),
                "description", film.getDescription(),
                "release_date", Date.valueOf(film.getReleaseDate()),
                "duration", film.getDuration(),
                "rating_id", id
        );
    }

    private void updateGenresAndRating(Film film) {
        Set<Genre> genres = film.getGenre();
        Set<Integer> likes = film.getLike();
        int id = film.getId();

        if (!genres.isEmpty()) {
            for (Genre genre : genres) {
                int genreId = genre.getId();
                String sqlQuery = "INSERT INTO genre_to_film (film_id, genre_id) " +
                        "VALUES (?, ?)";
                jdbcTemplate.update(sqlQuery, id, genreId);
            }
        }

        if (!likes.isEmpty()) {
            for (int userId : likes) {
                String sqlQuery = "INSERT INTO likes (film_id, user_id) " +
                        "VALUES (?, ?)";
                jdbcTemplate.update(sqlQuery, id, userId);
            }
        }
    }

}
