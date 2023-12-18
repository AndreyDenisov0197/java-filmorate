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
        updateFriends(user);
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
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM users;";
        List<Map<String,Object>> list = jdbcTemplate.queryForList(sql);
        list.forEach(rs -> {
            Date date = (Date) rs.get("birthday");
            LocalDate localDate = new java.sql.Date(date.getTime()).toLocalDate();

            User user = new User(
                    (String) rs.get("login"),
                    (String) rs.get("name"),
                    (String) rs.get("email"),
                    localDate
            );
            user.setId((Integer) rs.get("id"));
            getFriends(user);
            users.add(user);
        });
        return users;
    }


    @Override
    public User updateUser(User user) {
        int id = user.getId();

        String sql = "SELECT * FROM users WHERE id = ?";
        try {
            User userUpdate = jdbcTemplate.queryForObject(sql, getUserMapper(), id);
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(String.format("User с ID=%d не существует", id));
        }


        String sqlQuery = "UPDATE users SET " +
                "login = ?, name = ?, email = ?, " +
                "birthday = ? WHERE id = ?";

        int result = jdbcTemplate.update(sqlQuery,
                user.getLogin(),
                user.getName(),
                user.getEmail(),
                user.getBirthday(),
                id);

        if (result == 1) {
            jdbcTemplate.update("DELETE FROM friends WHERE user_id = ?;", id);
            updateFriends(user);
        }
        return user;
    }

    @Override
    public User getUserByID(int id) {
        try {
            User user = jdbcTemplate.queryForObject(
                    "SELECT id, name, email, login, birthday " +
                            "FROM users WHERE id = ?;", getUserMapper(), id);
            getFriends(user);
            return user;
        } catch (EmptyResultDataAccessException e) {
            throw new ObjectNotFoundException(String.format("User с ID=%d не существует", id));
        }



    }

    @Override
    public void updateFriends(User user) {
        Set<User> friends = user.getFriends();
        int id = user.getId();

        if (friends.isEmpty()) {
            return;
        }

        for (User u : friends) {
            try {
                String sqlQuery = "SELECT status FROM friends WHERE user_id = ? AND friend_id = ?;";
                boolean status = Boolean.FALSE.equals(jdbcTemplate.queryForObject(sqlQuery, Boolean.class, u.getId(), id));
                if (status) {
                    String sql = "UPDATE friends SET status = ? WHERE user_id = ? AND friend_id = ?;";
                    jdbcTemplate.update(sql, id, u.getId(), true);
                    jdbcTemplate.update(sql, u.getId(), id, true);
                } /*else {
                    String sql = "MERGE INTO friends (user_id, friend_id, status) " +
                            "VALUES (?, ?, false)";
                    jdbcTemplate.update(sql, id, u.getId());
                }*/
            } catch (EmptyResultDataAccessException e) {
                String sql = "INSERT INTO friends (user_id, friend_id, status) " +
                        "VALUES (?, ?, false)";
                jdbcTemplate.update(sql, id, u.getId());

            }


        }
    }

    private static Map<String, Object> userToMap(User user) {
        return Map.of(
                "login", user.getLogin(),
                "name", user.getName(),
                "email", user.getEmail(),
                "birthday", Date.valueOf(user.getBirthday())
        );
    }

    private void getFriends(User user) {
        int id = user.getId();
        List<Integer> friendsList = jdbcTemplate.queryForList(
                "SELECT friend_id FROM friends WHERE user_id = ?;", Integer.class, id);

        List<User> friends = new ArrayList<>();
        for (int friendId : friendsList) {
            User userFriends = jdbcTemplate.queryForObject(
                    "SELECT id, name, email, login, birthday " +
                            "FROM users WHERE id = ?;", getUserMapper(), friendId);

            if (userFriends != null) {
                friends.add(userFriends);
            }
        }
        user.setFriends(new HashSet<>(friends));
    }

    private static RowMapper<User> getUserMapper() {
        return (rs, rowNum) -> {
            /*User user = new User(rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("login"),
                    rs.getDate("birthday").toLocalDate()
            );*/

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
