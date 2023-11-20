package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
@SuperBuilder
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
