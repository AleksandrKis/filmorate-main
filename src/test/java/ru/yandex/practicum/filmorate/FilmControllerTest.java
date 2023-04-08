package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.film.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static ru.yandex.practicum.filmorate.controllers.validate.FilmValidation.validation;

public class FilmControllerTest {

//    @Test
//    public void invalidFieldsFilmTest() {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        Validator validator = factory.getValidator();
//        Film film = Film.builder()
//                .name("")
//                .description("YandexPracticum".repeat(14))
//                .duration(-200)
//                .releaseDate(null).build();
//        Set<ConstraintViolation<Film>> violations = validator.validate(film);
//        assertEquals(violations.size(), 4);
//        violations.forEach(f -> System.out.println("Validation error :"
//                + f.getPropertyPath() + "-" + f.getMessage()));
//        violations.clear();
//    }

    @Test
    public void setEmptyNameFilmValidate() {
        Film film = Film.builder()
                .name("").build();
        Exception exception = assertThrows(ValidationException.class, () -> validation(film));
        assertEquals("Bad film name", exception.getMessage());
    }

    @Test
    public void setMachDescriptionFilmValidate() {
        Film film = Film.builder()
                .name("YandexPracticum")
                .description("YandexPracticum".repeat(14)).build();
        Exception exception = assertThrows(ValidationException.class, () -> validation(film));
        assertEquals("Too mach description", exception.getMessage());
    }

    @Test
    public void setOldReleaseDateFilmValidate() {
        Film film = Film.builder()
                .name("YandexPracticum")
                .description("YandexPracticum".repeat(12))
                .releaseDate(LocalDate.of(1890, 03, 25)).build();
        Exception exception = assertThrows(ValidationException.class, () -> validation(film));
        assertEquals("Very old film", exception.getMessage());
    }

    @Test
    public void setNegativeDurationFilmValidate() {
        Film film = Film.builder()
                .name("YandexPracticum")
                .description("YandexPracticum".repeat(12))
                .releaseDate(LocalDate.of(1990, 03, 25))
                .duration(-200).build();
        Exception exception = assertThrows(ValidationException.class, () -> validation(film));
        assertEquals("Duration must be a positive", exception.getMessage());
    }
}
