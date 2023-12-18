package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@ToString
public class Film extends Entity {
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Set<Integer> like;
    private Set<Genre> genre;
    private Mpa mpa;

  /*  @Builder
    public Film(String name, String description, LocalDate releaseDate, Integer duration, Mpa mpa) {
        super(name);
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.like = new HashSet<>();
        this.genre = new HashSet<>();
        this.mpa = mpa;
    }*/

    @Builder
    public Film(String name, String description, LocalDate releaseDate, Integer duration, Mpa mpa, Set<Genre> genre) {
        super(name);
        this.description = description;
        this.releaseDate = releaseDate;
        this.duration = duration;
        this.like = new HashSet<>();
        this.genre = genre;
        this.mpa = mpa;
    }
/*    public Film setId(int id) {
        this.id = id;
        return this;
    }*/

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
