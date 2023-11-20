package ru.yandex.practicum.filmorate.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
public class Film extends Entity implements Comparable<Film> {
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Set<Integer> like;

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

    @Override
    public int compareTo(Film o) {
        return this.getLike().size() - o.getLike().size();
    }
}
