package ru.yandex.practicum.filmorate.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Data
public class Mpa {
    private Integer id;
    private String name;

    public Mpa(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
