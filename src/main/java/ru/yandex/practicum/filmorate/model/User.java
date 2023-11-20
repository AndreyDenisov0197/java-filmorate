package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Setter
@Getter
public class User extends Entity {
    private String email;
    private String login;
    private LocalDate birthday;
    private Set<Integer> friends;

    @Builder
    public User(String name, String email, String login, LocalDate birthday) {
        super(name);
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }

    public void addFriends(Integer id) {
        friends.add(id);
    }

    public void removeFriends(Integer id) {
        friends.remove(id);
    }
}
