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
                .birthday(LocalDate.parse("1997-01-15"))
                .build();

        user.setName("name3");

        User postUser = userController.addFile(user);
        user.setId(1);
        Assertions.assertEquals(user, postUser, "User не совпвдвет");
    }

    @Test
    void checkEmailEmpty() {
        User user = User.builder()
                .email("")
                .login("login")
                .birthday(LocalDate.parse("1997-01-15"))
                .build();

        user.setName("name");


        Assertions.assertThrows(ValidationException.class, () -> userController.addFile(user),
                "Ошибка пустой Email.");
    }

    @Test
    void checkEmailNull() {
        User user = User.builder()
                .email(null)
                .login("login1")
                .birthday(LocalDate.parse("1997-01-15"))
                .build();

        user.setName("name1");


        Assertions.assertThrows(ValidationException.class, () -> userController.addFile(user),
                "Ошибка Email null.");
    }

    @Test
    void checkEmailWithoutSymbol() {
        User user = User.builder()
                .email("denisovad.yandex.ru")
                .login("login2")
                .birthday(LocalDate.parse("1997-01-15"))
                .build();

        user.setName("name2");

        Assertions.assertThrows(ValidationException.class, () -> userController.addFile(user),
                "Ошибка Email без @.");
    }

    @Test
    void checkEmailBlank() {
        User user = User.builder()
                .email("   ")
                .login("login3")
                .birthday(LocalDate.parse("1997-01-15"))
                .build();

        user.setName("name3");

        Assertions.assertThrows(ValidationException.class, () -> userController.addFile(user),
                "Ошибка пустой Email с пробелами.");
    }



    @Test
    void checkLoginNull() {
        User user = User.builder()
                .email("denisov@yandex.ru")
                .login(null)
                .birthday(LocalDate.parse("1997-01-15"))
                .build();

        user.setName("name3");

        Assertions.assertThrows(ValidationException.class, () -> userController.addFile(user),
                "Ошибка Login == null.");
    }

    @Test
    void checkLoginBlank() {
        User user = User.builder()
                .email("denisov@yandex.ru")
                .login(" ")
                .birthday(LocalDate.parse("1997-01-15"))
                .build();

        user.setName("name3");

        Assertions.assertThrows(ValidationException.class, () -> userController.addFile(user),
                "Ошибка пустой Login с пробелами.");
    }

    @Test
    void checkBirthdayDateFuture() {
        User user = User.builder()
                .email("denisov@yandex.ru")
                .login("login")
                .birthday(LocalDate.now().plusMonths(1))
                .build();

        user.setName("name3");

        Assertions.assertThrows(ValidationException.class, () -> userController.addFile(user),
                "Ошибка день рождение в будущем.");
    }

    @Test
    void checkNameNull() {
        User user = User.builder()
                .email("denisov@yandex.ru")
                .login("login")
                .birthday(LocalDate.parse("1997-01-15"))
                .build();

        user.setName(null);

        User postUser = userController.addFile(user);
        user.setId(1);
        user.setName(user.getLogin());
        Assertions.assertEquals(user, postUser, "User не совпвдвет");
    }

    @Test
    void checkNameBlank() {
        User user = User.builder()
                .email("denisov@yandex.ru")
                .login("login")
                .birthday(LocalDate.parse("1997-01-15"))
                .build();

        user.setName("");

        User postUser = userController.addFile(user);
        user.setId(1);
        user.setName(user.getLogin());
        Assertions.assertEquals(user, postUser, "User не совпвдвет");
    }

    @Test
    void checkNameBlank2() {
        User user = User.builder()
                .email("denisov@yandex.ru")
                .login("login")
                .birthday(LocalDate.parse("1997-01-15"))
                .build();

        user.setName("  ");

        User postUser = userController.addFile(user);
        user.setId(1);
        user.setName(user.getLogin());
        Assertions.assertEquals(user, postUser, "User не совпвдвет");
    }
}
