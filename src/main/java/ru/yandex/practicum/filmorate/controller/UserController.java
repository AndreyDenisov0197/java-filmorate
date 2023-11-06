package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
public class UserController {
    private final Map<Integer, User> allUser = new HashMap<>();
    private int id = 0;

    @GetMapping("/users")
    public List<User> getUsers() {
        return new ArrayList<>(allUser.values());
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        validate(user);
        user.setId(++id);
        int id = user.getId();
        allUser.put(id, user);
        log.info("Добавили пользователяс ID-{}", id);
        return allUser.get(id);
    }

    @PutMapping("/users/{id}")
    public User updateUser(@RequestBody User user) {
        int id = user.getId();
        validate(user);
        if (allUser.containsKey(id)) {
            allUser.put(id, user);
            log.info("Обновили данные пользователя под ID-{}", id);
        } else {
            log.warn("Обновление невозможно. Пользователя под ID-{} не существует", id);
        }
        return allUser.get(id);
    }

    private void validate(User user) {
        String email = user.getEmail();
        if (email == null || !email.contains("@") || email.isBlank()) {
            log.error("Email пустой или не содержит символ @.");
            throw new ValidationException("Email пустой или не содержит символ @.");
        }

        String login = user.getLogin();
        if (login == null || login.contains(" ")) {
            log.error("Логин пустой или содержит пробелы.");
            throw new ValidationException("Логин пустой или содержит пробелы.");
        }

        LocalDate date = LocalDate.now();
        if (date.isBefore(user.getBirthday())) {
            log.error("День рождения не может быть в будущем времени.");
            throw new ValidationException("День рождения не может быть в будущем времени.");
        }

        String name = user.getName();
        if (name == null || name.isBlank()) {
            user.setName(user.getLogin());
            log.warn("Name пустой. Заменили name на login");
        }
    }
}
