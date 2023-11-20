package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserStorage userStorage;

    public void addFriends(int id, int friendId) {
        User user = userStorage.getUserByID(id);
        if (user == null) {
            throw new ObjectNotFoundException(String.format("Пользователя с ID=%d не существует", id));
        }
        user.addFriends(friendId);

        User friends = userStorage.getUserByID(friendId);
        if (friends == null) {
            throw new ObjectNotFoundException(String.format("Пользователя с ID=%d не существует", friendId));
        }
        friends.addFriends(id);
    }

    public void removeFriends(int id, int friendId) {
        User user = userStorage.getUserByID(id);
        if (user == null) {
            throw new ObjectNotFoundException(String.format("Пользователя с ID=%d не существует", id));
        }
        user.removeFriends(friendId);

        User friends = userStorage.getUserByID(friendId);
        if (friends == null) {
            throw new ObjectNotFoundException(String.format("Пользователя с ID=%d не существует", friendId));
        }
        friends.removeFriends(id);
    }

    public List<User> getFriends(int id) {
        User user = userStorage.getUserByID(id);

        if (user == null) {
            throw new ObjectNotFoundException(String.format("Пользователя с ID=%d не существует", id));
        }

        List<User> friends = new ArrayList<>();

        for (int friendId : user.getFriends()) {
            friends.add(userStorage.getUserByID(friendId));
        }

        return friends;
    }

    public List<User> getMutualFriends(int id, int otherId) {
        User user1 = userStorage.getUserByID(id);
        if (user1 == null) {
            throw new ObjectNotFoundException(String.format("Пользователя с ID=%d не существует", id));
        }

        User user2 = userStorage.getUserByID(otherId);
        if (user2 == null) {
            throw new ObjectNotFoundException(String.format("Пользователя с ID=%d не существует", otherId));
        }

        Set<Integer> users = user1.getFriends();
        Set<Integer> otherUsers = user2.getFriends();
        List<User> friends = new ArrayList<>();

        for (int userId : users) {
            if (otherUsers.contains(userId)) {
                friends.add(userStorage.getUserByID(userId));
            }
        }
        return friends;
    }

    public List<User> getUser() {
        return userStorage.getUser();
    }

    public User addUser(User user) {
        return userStorage.addUser(user);
    }

    public User updateUser(User user) {
        return userStorage.updateUser(user);
    }

    public User getUserByID(int id) {
        return userStorage.getUserByID(id);
    }
}
