package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/users")
    public List<User> getUser() {
        log.info("GET /users");
        return userService.getUser();
    }

    @PostMapping("/users")
    public User addUser(@RequestBody User user) {
        log.info("POST /users");
        validate(user);
        return userService.addUser(user);
    }

    @PutMapping("/users")
    public User updateUser(@RequestBody User user) {
        log.info("PUT /users");
        validate(user);
        return userService.updateUser(user);
    }

    @GetMapping("/users/{id}")
    public User getUserById(@PathVariable int id) {
        log.info("GET /users/{}", id);
        return userService.getUserByID(id);
    }

    @PutMapping("/users/{id}/friends/{friendId}")
    public void addFriends(@PathVariable int id, @PathVariable int friendId) {
        log.info("/users/{}/friends/{}", id, friendId);
        userService.addFriends(id, friendId);
    }

    @DeleteMapping("/users/{id}/friends/{friendId}")
    public void removeFriends(@PathVariable int id, @PathVariable int friendId) {
        log.info("/users/{}/friends/{}", id, friendId);
        userService.removeFriends(id, friendId);
    }

    @GetMapping("/users/{id}/friends")
    public List<User> getFriends(@PathVariable int id) {
        log.info("/users/{}/friends", id);
        return userService.getFriends(id);
    }

    @GetMapping("/users/{id}/friends/common/{otherId}")
    public List<User> getMutualFriends(@PathVariable int id, @PathVariable int otherId) {
        return userService.getMutualFriends(id, otherId);
    }

    protected void validate(User user) {
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
