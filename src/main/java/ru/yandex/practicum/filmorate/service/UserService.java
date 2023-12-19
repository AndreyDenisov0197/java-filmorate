package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.ObjectNotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Service
@RequiredArgsConstructor
@Qualifier("userDbStorage")
public class UserService {
    private final UserStorage userStorage;

    public List<User> getFriends(int id) {
        return userStorage.getAllUserFriends(id);
    }

    public List<User> getMutualFriends(int id, int otherId) {
        return userStorage.getUserCommonFriends(id, otherId);
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

    public User getUserById(int id) {
        return userStorage.getUserById(id);
    }

    public void addFriends(int userId, int friendId) {
        userStorage.putNewFriend(userId, friendId);
    }

    public void removeFriends(int userId, int friendId) {
        userStorage.deleteFriend(userId, friendId);
    }
}
