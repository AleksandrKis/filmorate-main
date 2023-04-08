package ru.yandex.practicum.filmorate.storage.film.dao;

import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;

import java.util.List;

public interface FilmStorage {

    Film create(Film film);

    Film update(Film film);

    Film findFilmById(Integer id);

    void addLikeFilmById(Integer filmId, Integer userId);

    void delLikeFilmById(Integer filmId, Integer userId);

    List<Film> getPopularFilmList(Integer count);

    List<Film> getAllFilms();

    Mpa getMpaById(Integer id);

    List<Mpa> getAllMpa();

    Genre getGenreById(Integer id);

    List<Genre> getAllGenre();
}
