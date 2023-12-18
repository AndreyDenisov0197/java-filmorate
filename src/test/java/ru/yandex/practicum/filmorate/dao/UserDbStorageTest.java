package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest // указываем, о необходимости подготовить бины для работы с БД
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class UserDbStorageTest {
    private final JdbcTemplate jdbcTemplate;
    private UserDbStorage userStorage;

    @BeforeEach
    public void beforeEach() {
        userStorage = new UserDbStorage(jdbcTemplate);

    }

    @Test
    public void testFindUserById() {
        // Подготавливаем данные для теста
        User newUser = new User( "Ivan Petrov", "user@email.ru", "vanya123",
                LocalDate.of(1990, 1, 1));
        User user1 = userStorage.addUser(newUser); //тауже протестировали в данном методе

        // вызываем тестируемый метод
        User savedUser = userStorage.getUserByID(user1.getId());

        // проверяем утверждения
        assertThat(savedUser)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(newUser);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testDeleteUser() {
        User newUser = new User( "Ivan Denisov", "user3@email.ru", "vanya000",
                LocalDate.of(1991, 11, 12));

        User user1 = userStorage.addUser(newUser);

        User savedUser = userStorage.getUserByID(user1.getId());

        assertThat(savedUser)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(newUser);        // и сохраненного пользователя - совпадают

        userStorage.deleteUser(savedUser);

        Assertions.assertThrows(EmptyResultDataAccessException.class, () -> {
            userStorage.getUserByID(1);
        });
    }

    @Test
    public void testGetUsers() {
        User user1 = new User("Ivan Denisov", "user3@email.ru", "vanya000",
                LocalDate.of(1991, 11, 12));
        User user2 = new User("Ivan Petrov", "user@email.ru", "vanya123",
                LocalDate.of(1990, 1, 1));
        User user3 = new User("Andrey Petrov", "user1@email.ru", "vanya3123",
                LocalDate.of(1999, 5, 21));
        userStorage.addUser(user1);
        userStorage.addUser(user2);
        userStorage.addUser(user3);

        List<User> users = new ArrayList<>();
        users.add(user1);
        users.add(user2);
        users.add(user3);

        List<User> newUsers = userStorage.getUser();

        assertThat(newUsers)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(users);
    }

    @Test
    public void testUpdateUser() {
        User user1 = new User("Ivan Denisov", "user3@email.ru", "vanya000",
                LocalDate.of(1991, 11, 12));
        User userAdd = userStorage.addUser(user1);

        User newUser = userStorage.getUserByID(userAdd.getId());
        newUser.setName("Dmitriy Zotov");

        userStorage.updateUser(newUser);
        User updateUser = userStorage.getUserByID(userAdd.getId());

        assertThat(updateUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }
}