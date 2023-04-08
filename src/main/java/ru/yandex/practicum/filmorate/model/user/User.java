package ru.yandex.practicum.filmorate.model.user;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@Builder
public class User {
    private int id;
    private String email;
    private String login;
    private String name;
    private LocalDate birthday;
    private final Set<Friendship> friends = new HashSet<>();

}
