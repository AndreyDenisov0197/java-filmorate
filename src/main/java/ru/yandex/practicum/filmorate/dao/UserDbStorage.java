package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;


@RequiredArgsConstructor
@Repository("userDbStorage")
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public User addUser(User user) {
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        int id = insert.executeAndReturnKey(userToMap(user)).intValue();
        user.setId(id);
        return user;
    }

    @Override
    public void deleteUser(User user) {
        int id = user.getId();
        jdbcTemplate.update("DELETE FROM users WHERE id = ?;", id);
        jdbcTemplate.update("DELETE FROM friends WHERE user_id = ? OR friend_id = ?;", id, id);
    }

    @Override
    public List<User> getUser() {
        return new LinkedList<>(jdbcTemplate.query("SELECT * FROM users;", getUserMapper()));
    }


    @Override
    public User updateUser(User user) {
        int id = user.getId();
        getUserById(id);

        String sqlQuery = "UPDATE users SET " +
                "login = ?, name = ?, email = ?, " +
                "birthday = ? WHERE id = ?";

        jdbcTemplate.update(sqlQuery,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                id);
        return user;
    }

    @Override
    public User getUserById(int id) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM users WHERE id = ?;", getUserMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(String.format("User с ID=%d не существует", id));
        }
    }

    @Override
    public List<User> getAllUserFriends(Integer userId) {
        getUserById(userId);
        return new LinkedList<>(jdbcTemplate.query("select * from users where id IN (select friend_id from friends where user_id = ?) ORDER BY users.id;", getUserMapper(), userId));
    }

    @Override
    public List<User> getUserCommonFriends(Integer userId, Integer otherId) {
        getUserById(userId);
        getUserById(otherId);
        return new LinkedList<>(jdbcTemplate.query("SELECT * " +
                "FROM users WHERE users.id IN ( " +
                "SELECT friend_id " +
                "FROM friends " +
                "WHERE friends.user_id = " + userId + " " +
                "INTERSECT " +
                "SELECT friend_id " +
                "FROM friends " +
                "WHERE friends.user_id = " + otherId + ") " +
                "AND users.id NOT IN (" + userId + ", " + otherId + ");", getUserMapper()));
    }

    @Override
    public void putNewFriend(Integer userId, Integer friendId) {
        getUserById(userId);
        getUserById(friendId);
        String sql = "INSERT INTO friends (user_id, friend_id) VALUES (?, ?)";
        jdbcTemplate.update(sql, userId, friendId);

    }

    @Override
    public void deleteFriend(Integer userId, Integer friendId) {
        getUserById(userId);
        getUserById(friendId);
        String sql = "DELETE FROM friends WHERE user_id = ? AND friend_id = ?;";
        jdbcTemplate.update(sql, userId, friendId);
    }

    private static Map<String, Object> userToMap(User user) {
        return Map.of(
                "login", user.getLogin(),
                "name", user.getName(),
                "email", user.getEmail(),
                "birthday", Date.valueOf(user.getBirthday())
        );
    }

    private static RowMapper<User> getUserMapper() {
        return (rs, rowNum) -> {
            User user = new User(
                    rs.getString("login"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getDate("birthday").toLocalDate()
            );
            user.setId(rs.getInt("id"));
            return user;
        };
    }
}
