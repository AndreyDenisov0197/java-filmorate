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

    public void addFriends(int id, int friendId) { // изменить логику добавления в друзья + обновить БД
        User user = userStorage.getUserByID(id);
        if (user == null) {
            throw new ObjectNotFoundException(String.format("Пользователя с ID=%d не существует", id));
        }

        User friends = userStorage.getUserByID(friendId);
        if (friends == null) {
            throw new ObjectNotFoundException(String.format("Пользователя с ID=%d не существует", friendId));
        }
        user.addFriends(friends);
        userStorage.updateUser(user);
        //userStorage.updateFriends(user);
    }

    public void removeFriends(int id, int friendId) {
        User user = userStorage.getUserByID(id);
        if (user == null) {
            throw new ObjectNotFoundException(String.format("Пользователя с ID=%d не существует", id));
        }

        User friends = userStorage.getUserByID(friendId);
        if (friends == null) {
            throw new ObjectNotFoundException(String.format("Пользователя с ID=%d не существует", friendId));
        }

        user.removeFriends(friends);
/*
        User friends = userStorage.getUserByID(friendId);
        if (friends == null) {
            throw new ObjectNotFoundException(String.format("Пользователя с ID=%d не существует", friendId));
        }
        friends.removeFriends(id);*/
        userStorage.updateFriends(user);
    }

    public List<User> getFriends(int id) {
        User user = userStorage.getUserByID(id);

        if (user == null) {
            throw new ObjectNotFoundException(String.format("Пользователя с ID=%d не существует", id));
        }

        return new ArrayList<>(user.getFriends());
/*
        List<User> friends = new ArrayList<>();

        for (int friendId : user.getFriends()) {
            friends.add(userStorage.getUserByID(friendId));
        }

        return friends;*/
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

        Set<User> users = user1.getFriends();
        Set<User> otherUsers = user2.getFriends();
        List<User> friends = new ArrayList<>();

        for (User u : users) {
            if (otherUsers.contains(u)) {
                friends.add(u);
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
