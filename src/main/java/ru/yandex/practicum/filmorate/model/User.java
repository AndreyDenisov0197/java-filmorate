package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class User extends Entity {
    private String email;
    private String login;
    private LocalDate birthday;


    @Builder
    public User(String login, String name, String email, LocalDate birthday) {
        super(name);
        this.email = email;
        this.login = login;
        this.birthday = birthday;
    }
}
