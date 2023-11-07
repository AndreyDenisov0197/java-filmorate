package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Data
@Builder
public class User extends Entity {
    private String email;
    private String login;
    private LocalDate birthday;
}
