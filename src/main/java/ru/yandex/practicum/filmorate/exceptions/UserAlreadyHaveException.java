package ru.yandex.practicum.filmorate.exceptions;

public class UserAlreadyHaveException extends RuntimeException {
    public UserAlreadyHaveException(String message) {
        super(message);
    }
}
