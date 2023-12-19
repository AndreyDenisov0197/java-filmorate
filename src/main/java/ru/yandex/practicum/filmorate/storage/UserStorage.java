package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {

    User addUser(User user);

    void deleteUser(User user);

    List<User> getUser();

    User updateUser(User user);

    User getUserById(int id);

    List<User> getAllUserFriends(Integer userId);

    List<User> getUserCommonFriends(Integer userId, Integer otherId);

    void putNewFriend(Integer userId, Integer friendId);

    void deleteFriend(Integer userId, Integer friendId);
}
