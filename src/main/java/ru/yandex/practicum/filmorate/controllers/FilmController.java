package ru.yandex.practicum.filmorate.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.List;


@RestController
@RequestMapping("/films")
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;

    @PostMapping
    public Film create(@RequestBody Film film) {
        return filmService.create(film);
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        return filmService.update(film);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable("id") Integer id) {
        return filmService.findFilmById(id);
    }

    @PutMapping("/{id}/like/{userId}")
    public void likeFilmById(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) {
        filmService.addLikeFilmById(filmId, userId);

    }

    @DeleteMapping("/{id}/like/{userId}")
    public void delLikeFilmById(@PathVariable("id") Integer filmId, @PathVariable("userId") Integer userId) {
        filmService.delLikeFilmById(filmId, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilmList(
            @RequestParam(value = "count", defaultValue = "10", required = false) Integer count) {
        return filmService.getPopularFilmList(count);
    }

    @GetMapping
    public List<Film> getAllFilms() {
        return filmService.filmsList();
    }

}
