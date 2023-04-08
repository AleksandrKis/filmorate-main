package ru.yandex.practicum.filmorate.exceptions;

public class FilmAlreadyHaveException extends RuntimeException {
    public FilmAlreadyHaveException(String message) {
        super(message);
    }
}
