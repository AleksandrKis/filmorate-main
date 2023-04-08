package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;

@RestController
@RequestMapping("/genres")
@RequiredArgsConstructor
public class GenreController {
    private final FilmService filmService;

    @GetMapping("/{id}")
    public Genre getGenreById(@PathVariable("id") Integer id) {
        return filmService.getGenreById(id);
    }

    @GetMapping
    public List<Genre> getAllGenre() {
        return filmService.getAllGenre();
    }
}
