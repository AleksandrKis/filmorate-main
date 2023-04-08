package ru.yandex.practicum.filmorate.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(basePackages = "ru.yandex.practicum.filmorate")
public class ErrorHandler {

    @ExceptionHandler({FilmNotFoundException.class, UserNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(RuntimeException e) {
        return new ErrorResponse("Not found", e.getMessage());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleNotValid(RuntimeException e) {
        return new ErrorResponse("Validation error", e.getMessage());
    }

    @ExceptionHandler({FilmAlreadyHaveException.class, UserAlreadyHaveException.class})
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    public ErrorResponse handleAlreadyHave(RuntimeException e) {
        return new ErrorResponse("Already have", e.getMessage());
    }
}
