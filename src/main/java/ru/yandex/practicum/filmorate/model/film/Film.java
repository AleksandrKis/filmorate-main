package ru.yandex.practicum.filmorate.model.film;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@Builder
public class Film {
    private int id;
    private String name;
    private String description;
    private LocalDate releaseDate;
    private long duration;
    private final Set<Integer> likes = new HashSet<>();
    private Mpa mpa;
    private final List<Genre> genres = new ArrayList<>();

}
