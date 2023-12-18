package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Entity {
    protected int id;
    protected String name;

    public Entity(String name) {
        this.name = name;
    }

    public Entity(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
