package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
public class UserController extends Controller<User>{

    @Override
    @GetMapping("/users")
    public List<User> getFile() {
        return super.getFile();
    }

    @Override
    @PostMapping("/users")
    public User addFile(@RequestBody User file) {
        return super.addFile(file);
    }

    @Override
    @PutMapping("/users")
    public User updateFile(@RequestBody User file) {
        return super.updateFile(file);
    }


    @Override
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
