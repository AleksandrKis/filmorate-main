package ru.yandex.practicum.filmorate.controllers.validate;

import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.time.LocalDate;

public class UserValidation {
    public static boolean validation(User user) {
        if (user.getEmail() == null || (!user.getEmail().contains("@"))) {
            throw new ValidationException("email format is wrong");
        }
        if (user.getLogin().contains(" ") || user.getLogin().isEmpty()) {
            throw new ValidationException("login format is wrong");
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Birthday can't be");
        }
        return true;
    }
}
