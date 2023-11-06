package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

public class UserControllerTest {
    UserController userController;

    @BeforeEach
    void beforeEach() {
        userController = new UserController();
    }

    @Test
    void checkUser() {
        User user = User.builder()
                .email("denisov@yandex.ru")
                .login("login3")
                .name("name3")
                .birthday(LocalDate.parse("1997-01-15"))
                .build();

        User postUser = userController.addUser(user);
        user.setId(1);
        Assertions.assertEquals(user, postUser, "User не совпвдвет");
    }

    @Test
    void checkEmailEmpty() {
        User user = User.builder()
                .email("")
                .login("login")
                .name("name")
                .birthday(LocalDate.parse("1997-01-15"))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user),
                "Ошибка пустой Email.");
    }

    @Test
    void checkEmailNull() {
        User user = User.builder()
                .email(null)
                .login("login1")
                .name("name1")
                .birthday(LocalDate.parse("1997-01-15"))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user),
                "Ошибка Email null.");
    }

    @Test
    void checkEmailWithoutSymbol() {
        User user = User.builder()
                .email("denisovad.yandex.ru")
                .login("login2")
                .name("name2")
                .birthday(LocalDate.parse("1997-01-15"))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user),
                "Ошибка Email без @.");
    }

    @Test
    void checkEmailBlank() {
        User user = User.builder()
                .email("   ")
                .login("login3")
                .name("name3")
                .birthday(LocalDate.parse("1997-01-15"))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user),
                "Ошибка пустой Email с пробелами.");
    }



    @Test
    void checkLoginNull() {
        User user = User.builder()
                .email("denisov@yandex.ru")
                .login(null)
                .name("name3")
                .birthday(LocalDate.parse("1997-01-15"))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user),
                "Ошибка Login == null.");
    }

    @Test
    void checkLoginBlank() {
        User user = User.builder()
                .email("denisov@yandex.ru")
                .login(" ")
                .name("name3")
                .birthday(LocalDate.parse("1997-01-15"))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user),
                "Ошибка пустой Login с пробелами.");
    }

    @Test
    void checkBirthdayDateFuture() {
        User user = User.builder()
                .email("denisov@yandex.ru")
                .login("login")
                .name("name3")
                .birthday(LocalDate.now().plusMonths(1))
                .build();

        Assertions.assertThrows(ValidationException.class, () -> userController.addUser(user),
                "Ошибка день рождение в будущем.");
    }

    @Test
    void checkNameNull() {
        User user = User.builder()
                .email("denisov@yandex.ru")
                .login("login")
                .name(null)
                .birthday(LocalDate.parse("1997-01-15"))
                .build();

        User postUser = userController.addUser(user);
        user.setId(1);
        user.setName(user.getLogin());
        Assertions.assertEquals(user, postUser, "User не совпвдвет");
    }

    @Test
    void checkNameBlank() {
        User user = User.builder()
                .email("denisov@yandex.ru")
                .login("login")
                .name("")
                .birthday(LocalDate.parse("1997-01-15"))
                .build();

        User postUser = userController.addUser(user);
        user.setId(1);
        user.setName(user.getLogin());
        Assertions.assertEquals(user, postUser, "User не совпвдвет");
    }

    @Test
    void checkNameBlank2() {
        User user = User.builder()
                .email("denisov@yandex.ru")
                .login("login")
                .name(" ")
                .birthday(LocalDate.parse("1997-01-15"))
                .build();

        User postUser = userController.addUser(user);
        user.setId(1);
        user.setName(user.getLogin());
        Assertions.assertEquals(user, postUser, "User не совпвдвет");
    }
}
