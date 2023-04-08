package ru.yandex.practicum.filmorate.utilities;

import java.time.LocalDate;
import java.util.Map;

public class Constants {
    public static final LocalDate OLDEST_RELEASE = LocalDate.of(1895, 12, 28);
    public static final Map<Integer, String> MPA = Map.of(1, "G", 2, "PG", 3, "PG-13", 4, "R", 5, "NC-17");
    public static final Map<Integer, String> GENRE = Map.of(1, "Комедия", 2, "Драма", 3, "Мультфильм",
            4, "Триллер", 5, "Документальный", 6, "Боевик");

}
