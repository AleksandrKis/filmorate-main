package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exceptions.ValidationException;
import ru.yandex.practicum.filmorate.model.user.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.yandex.practicum.filmorate.controllers.validate.UserValidation.validation;

public class UserControllerTest {

//    @Test
//    public void invalidFieldsFilmTest() {
//        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
//        Validator validator = factory.getValidator();
//        User user = User.builder()
//                .email("practicum#email")
//                .login("")
//                .birthday(LocalDate.of(2024, 12, 31)).build();
//        Set<ConstraintViolation<User>> violations = validator.validate(user);
//        assertEquals(violations.size(), 3);
//        violations.forEach(f -> System.out.println("Validation error :"
//                + f.getPropertyPath() + "-" + f.getMessage()));
//        violations.clear();
//    }

    @Test
    public void emptyEmailTest() {
        User user = User.builder()
                .email("").build();
        Exception exception = assertThrows(ValidationException.class, () -> validation(user));
        assertEquals("email format is wrong", exception.getMessage());
    }

    @Test
    public void wrongFormatEmailTest() {
        User user = User.builder()
                .email("practicum#email").build();
        Exception exception = assertThrows(ValidationException.class, () -> validation(user));
        assertEquals("email format is wrong", exception.getMessage());
    }

    @Test
    public void emptyLoginTest() {
        User user = User.builder()
                .email("practicum@yandex.ru")
                .login("").build();
        Exception exception = assertThrows(ValidationException.class, () -> validation(user));
        assertEquals("login format is wrong", exception.getMessage());
    }

    @Test
    public void withBlankLoginTest() {
        User user = User.builder()
                .email("practicum@yandex.ru")
                .login("yandex practicum").build();
        Exception exception = assertThrows(ValidationException.class, () -> validation(user));
        assertEquals("login format is wrong", exception.getMessage());
    }

    @Test
    public void birthdayFutureTest() {
        User user = User.builder()
                .email("practicum@yandex.ru")
                .login("practicum")
                .birthday((LocalDate.of(2024, 12, 31))).build();
        Exception exception = assertThrows(ValidationException.class, () -> validation(user));
        assertEquals("Birthday can't be", exception.getMessage());
    }
}
