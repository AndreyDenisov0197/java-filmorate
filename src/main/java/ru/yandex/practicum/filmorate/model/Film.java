package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Film extends Entity {
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Set<Integer> like;
    //private Set<String> genre; // вопрос какой указать тип?
    //private String rating; // вопрос какой указать тип?

    @Builder
    public Film(String name, String description, LocalDate releaseDate, Integer duration) {
        super(name);
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.like = new HashSet<>();
    }

    public void addLike(Integer id) {
        like.add(id);
    }

    public void removeLike(Integer id) {
        like.remove(id);
    }

    public long getRate() {
        return like.size();
    }
}
