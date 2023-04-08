package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.storage.film.dao.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.dao.UserStorage;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    public Film create(Film film) {
        return filmStorage.create(film);
    }

    public Film update(Film film) {
        return filmStorage.update(film);
    }

    public List<Film> filmsList() {
        return filmStorage.getAllFilms();
    }

    public Film findFilmById(Integer id) {
        return filmStorage.findFilmById(id);
    }

    public void addLikeFilmById(Integer filmId, Integer userId) {
        filmStorage.addLikeFilmById(filmId, userId);
    }

    public void delLikeFilmById(Integer filmId, Integer userId) {
        filmStorage.delLikeFilmById(filmId, userId);
    }

    public List<Film> getPopularFilmList(Integer count) {
        return filmStorage.getPopularFilmList(count);
    }

    public Mpa getMpaById(Integer id) {
        return filmStorage.getMpaById(id);
    }

    public List<Mpa> getAllMpa() {
        return filmStorage.getAllMpa();
    }

    public Genre getGenreById(Integer id) {
        return filmStorage.getGenreById(id);
    }

    public List<Genre> getAllGenre() {
        return filmStorage.getAllGenre();
    }
}
