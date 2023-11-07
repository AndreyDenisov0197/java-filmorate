package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Entity;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public abstract class Controller<T extends Entity> {
    protected final Map<Integer, T> allFile = new HashMap<>();
    protected int index = 1;

    public List<T> getFile() {
        return new ArrayList<>(allFile.values());
    }

    public T addFile(@RequestBody T file) {
        validate(file);
        if (file.getId() == null) {
            while (allFile.containsKey(index)) {
                ++index;
            }
            file.setId(index);
        }
        int id = file.getId();
        allFile.put(id, file);
        log.info("Добавлен {}", file.getName());
        return allFile.get(id);
    }

    public T updateFile(@RequestBody T file) {
        validate(file);
        int id = file.getId();
        if (allFile.containsKey(id)) {
            file.setId(id);
            allFile.put(id, file);
            log.info("Обновили {}", file.getName());
        } else {
            log.warn("Обновление невозможно. {} не добавлен в коллекцию", file.getName());
            throw new ValidationException("Обновление невозможно. " + file.getName() + " не добавлен в коллекцию");
        }
        return allFile.get(file.getId());
    }

    protected abstract void validate(T file);
}
