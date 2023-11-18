package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder
public class User extends Entity {
    private String email;
    private String login;
    private LocalDate birthday;
    private Set<Integer> friends;

    public void addFriends(Integer id) {
        friends.add(id);
    }

    public void removeFriends(Integer id) {
        friends.remove(id);
    }
}
