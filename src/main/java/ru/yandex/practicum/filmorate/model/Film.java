package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class Film  extends Entity {
    private String description;
    private LocalDate releaseDate;
    private Integer duration;

}
