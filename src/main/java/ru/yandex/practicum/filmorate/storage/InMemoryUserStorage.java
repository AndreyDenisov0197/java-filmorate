package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    protected final Map<Integer, User> allUser = new HashMap<>();
    protected int index = 1;

    @Override
    public List<User> getUser() {
        return new ArrayList<>(allUser.values());
    }

    @Override
    public User getUserByID(int id) {
        User user = allUser.get(id);
        if (user == null) {
            throw new ObjectNotFoundException(String.format("Пользователя с ID=%d не существует", id));
        }
        return user;
    }

    @Override
    public void deleteUser(User user) {
        Integer id = user.getId();
        allUser.remove(id);
    }

    @Override
    public User addUser(User user) {
        if (user.getId() == null) {
            while (allUser.containsKey(index)) {
                ++index;
            }
            user.setId(index);
        }
        int id = user.getId();
        allUser.put(id, user);
        return allUser.get(id);
    }

    @Override
    public User updateUser(User user) {
        int id = user.getId();
        if (allUser.containsKey(id)) {
            user.setId(id);
            allUser.put(id, user);
        } else {
            throw new ValidationException("Обновление невозможно. " + user.getName() + " не добавлен в коллекцию");
        }
        return allUser.get(user.getId());
    }
}
