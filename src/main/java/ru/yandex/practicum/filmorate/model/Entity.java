package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public abstract class Entity {
    protected Integer id;
    protected String name;

}
