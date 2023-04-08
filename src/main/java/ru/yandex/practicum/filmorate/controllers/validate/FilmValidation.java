package ru.yandex.practicum.filmorate.controllers.validate;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;

import static ru.yandex.practicum.filmorate.utilities.Constants.OLDEST_RELEASE;

public class FilmValidation {
    public static boolean validation(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Bad film name");
        }
        if (film.getDescription().length() > 200) {
            throw new ValidationException("Too mach description");
        }
        if (film.getReleaseDate().isBefore(OLDEST_RELEASE)) {
            throw new ValidationException("Very old film");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Duration must be a positive");
        }
        return true;
    }
}
