package ru.yandex.practicum.filmorate.model;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@SuperBuilder
public class Film extends Entity implements Comparable<Film> {
    private String description;
    private LocalDate releaseDate;
    private Integer duration;
    private Set<Integer> like;



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
