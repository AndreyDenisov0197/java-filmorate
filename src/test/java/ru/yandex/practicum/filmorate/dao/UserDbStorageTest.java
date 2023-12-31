package ru.yandex.practicum.filmorate.dao;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
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
        User newUser = new User("vanya123", "Ivan Petrov", "user@email.ru",
                LocalDate.of(1990, 1, 1));
        User user1 = userStorage.addUser(newUser); //тауже протестировали в данном методе

        // вызываем тестируемый метод
        User savedUser = userStorage.getUserById(user1.getId());

        // проверяем утверждения
        assertThat(savedUser)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(newUser);        // и сохраненного пользователя - совпадают
    }

    @Test
    public void testDeleteUser() {
        User newUser = new User("vanya000", "Ivan Denisov", "user3@email.ru",
                LocalDate.of(1991, 11, 12));

        User user1 = userStorage.addUser(newUser);

        User savedUser = userStorage.getUserById(user1.getId());

        assertThat(savedUser)
                .isNotNull() // проверяем, что объект не равен null
                .usingRecursiveComparison() // проверяем, что значения полей нового
                .isEqualTo(newUser);        // и сохраненного пользователя - совпадают

        userStorage.deleteUser(savedUser);

        Assertions.assertThrows(ObjectNotFoundException.class, () -> {
            userStorage.getUserById(user1.getId());
        });
    }

    @Test
    public void testGetUsers() {
        User user1 = new User("vanya000", "Ivan Denisov", "user3@email.ru",
                LocalDate.of(1991, 11, 12));
        User user2 = new User("vanya123", "Ivan Petrov", "user@email.ru",
                LocalDate.of(1990, 1, 1));
        User user3 = new User("vanya3123", "Andrey Petrov", "user1@email.ru",
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
        User user1 = new User("vanya000", "Ivan Denisov", "user3@email.ru",
                LocalDate.of(1991, 11, 12));
        User userAdd = userStorage.addUser(user1);

        User newUser = userStorage.getUserById(userAdd.getId());
        newUser.setName("Dmitriy Zotov");

        userStorage.updateUser(newUser);
        User updateUser = userStorage.getUserById(userAdd.getId());

        assertThat(updateUser)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(newUser);
    }

    @Test
    public void testGetAllUserFriends() {
        User user1 = new User("vanya000", "Ivan Denisov", "user3@email.ru",
                LocalDate.of(1991, 11, 12));
        User userAdd1 = userStorage.addUser(user1);

        User user2 = new User("vanya000", "Ivan Denisov", "user3@email.ru",
                LocalDate.of(1991, 11, 12));
        User userAdd2 = userStorage.addUser(user2);

        User user3 = new User("vanya000", "Ivan Denisov", "user3@email.ru",
                LocalDate.of(1991, 11, 12));
        User userAdd3 = userStorage.addUser(user3);

        userStorage.putNewFriend(userAdd1.getId(), userAdd2.getId());
        userStorage.putNewFriend(userAdd1.getId(), userAdd3.getId());

        List<User> friends = userStorage.getAllUserFriends(userAdd1.getId());
        List<User> friendsList = new ArrayList<>();
        friendsList.add(userAdd2);
        friendsList.add(userAdd3);

        assertThat(friends)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(friendsList);
    }

    @Test
    public void testDeleteFriend() {
        User user1 = new User("vanya000", "Ivan Denisov", "user3@email.ru",
                LocalDate.of(1991, 11, 12));
        User userAdd1 = userStorage.addUser(user1);

        User user2 = new User("vanya000", "Ivan Denisov", "user3@email.ru",
                LocalDate.of(1991, 11, 12));
        User userAdd2 = userStorage.addUser(user2);

        User user3 = new User("vanya000", "Ivan Denisov", "user3@email.ru",
                LocalDate.of(1991, 11, 12));
        User userAdd3 = userStorage.addUser(user3);

        userStorage.putNewFriend(userAdd1.getId(), userAdd2.getId());
        userStorage.putNewFriend(userAdd1.getId(), userAdd3.getId());
        List<User> friends1 = userStorage.getAllUserFriends(userAdd1.getId());

        Assertions.assertEquals(friends1.size(), 2);

        userStorage.deleteFriend(userAdd1.getId(), userAdd2.getId());

        List<User> friends = userStorage.getAllUserFriends(userAdd1.getId());
        List<User> friendsList = new ArrayList<>();
        friendsList.add(userAdd3);

        Assertions.assertEquals(friends.size(), 1);
        assertThat(friends)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(friendsList);
    }

    @Test
    public void testGetUserCommonFriends() {
        User user1 = new User("vanya000", "Ivan Denisov", "user3@email.ru",
                LocalDate.of(1991, 11, 12));
        User userAdd1 = userStorage.addUser(user1);

        User user2 = new User("vanya000", "Ivan Denisov", "user3@email.ru",
                LocalDate.of(1991, 11, 12));
        User userAdd2 = userStorage.addUser(user2);

        User user3 = new User("vanya000", "Ivan Denisov", "user3@email.ru",
                LocalDate.of(1991, 11, 12));
        User userAdd3 = userStorage.addUser(user3);

        User user4 = new User("vanya000", "Ivan Denisov", "user3@email.ru",
                LocalDate.of(1991, 11, 12));
        User userAdd4 = userStorage.addUser(user4);

        userStorage.putNewFriend(userAdd1.getId(), userAdd2.getId());
        userStorage.putNewFriend(userAdd1.getId(), userAdd3.getId());
        userStorage.putNewFriend(userAdd1.getId(), userAdd4.getId());
        userStorage.putNewFriend(userAdd2.getId(), userAdd3.getId());
        userStorage.putNewFriend(userAdd2.getId(), userAdd4.getId());

        List<User> friends = userStorage.getUserCommonFriends(userAdd1.getId(), userAdd2.getId());
        List<User> friendsList = new ArrayList<>();
        friendsList.add(userAdd3);
        friendsList.add(userAdd4);

        assertThat(friends)
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(friendsList);

    }

}
