package ru.yandex.practicum.filmorate.storage.film.daoImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.FilmNotFoundException;
import ru.yandex.practicum.filmorate.exceptions.UserAlreadyHaveException;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.film.dao.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.dao.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.filmorate.controllers.validate.FilmValidation.validation;
import static ru.yandex.practicum.filmorate.storage.SqlConstants.*;
import static ru.yandex.practicum.filmorate.utilities.Constants.GENRE;
import static ru.yandex.practicum.filmorate.utilities.Constants.MPA;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<Film> getAllFilms() {
        List<Film> allFilms = new ArrayList<>();
        String sql = "select * from FILMS";
        allFilms = jdbcTemplate.query(sql, (rs, rowNum) -> collectFilm(rs));
        log.debug("All Film List collected ");
        return allFilms;
    }

    @Override
    public Film findFilmById(Integer id) {
        String sql = "select * from FILMS where FILM_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> collectFilm(rs), id)
                .stream().findFirst().orElseThrow(() -> {
                    throw new FilmNotFoundException("Film not found.by Id " + id);
                });
    }

    @Override
    public Film create(Film film) {
        if (validation(film)) {
            KeyHolder keyHolder = new GeneratedKeyHolder();
            collectStmt(film, CREATE_FILM, keyHolder);
            film.setId(keyHolder.getKey().intValue());
            film.setMpa(makeRating(film.getMpa().getId()));
            setGenre(film);
        }
        log.debug("Film " + film.getName() + " collected !");
        return film;
    }

    @Override
    public Film update(Film film) {
        findFilmById(film.getId());
        jdbcTemplate.update(UPDATE_FILM, film.getName(), film.getDescription(), film.getReleaseDate(),
                film.getDuration(), film.getMpa().getId(), film.getId());
        film.getLikes().addAll(getLikesByFilm(film.getId()));
        film.setMpa(makeRating(film.getMpa().getId()));
        setGenre(film);
        log.debug("Film with ID - " + film.getId() + " updated !");
        return film;
    }

    @Override
    public void addLikeFilmById(Integer filmId, Integer userId) {
        Film film = findFilmById(filmId);
        User user = userStorage.findUserById(userId);
        if (!getLikesByFilm(filmId).contains(userId)) {
            String sql = "insert into LIKES (FILM_ID, USER_ID) VALUES ( ?, ?)";
            jdbcTemplate.update(sql, filmId, userId);
            film.getLikes().add(userId);
            log.debug("Film " + film.getName() + " get Like !");
        } else {
            throw new UserAlreadyHaveException(" Your like, already have by " + film.getName() + " film");
        }
    }

    @Override
    public void delLikeFilmById(Integer filmId, Integer userId) {
        Film film = findFilmById(filmId);
        User user = userStorage.findUserById(userId);
        if (getLikesByFilm(filmId).contains(userId)) {
            String sql = "delete from LIKES WHERE FILM_ID = ? and USER_ID = ?";
            jdbcTemplate.update(sql, filmId, userId);
            log.debug("Film " + film.getName() + " get delete Like !");
        } else {
            throw new UserNotFoundException("That's film haven't your like!");
        }
    }

    @Override
    public List<Film> getPopularFilmList(Integer count) {
        return jdbcTemplate.query(POPULAR_FILMS, (rs, rowNum) -> collectFilm(rs), count);
    }

    @Override
    public Mpa getMpaById(Integer id) {
        return makeRating(id);
    }

    @Override
    public List<Mpa> getAllMpa() {
        List<Mpa> res = new ArrayList<>();
        MPA.keySet().stream().sorted().forEach(id -> res.add(makeRating(id)));
        return res;
    }

    @Override
    public Genre getGenreById(Integer id) {
        return makeGenre(id);
    }

    @Override
    public List<Genre> getAllGenre() {
        List<Genre> res = new ArrayList<>();
        GENRE.keySet().stream().sorted().forEach(id -> res.add(makeGenre(id)));
        return res;
    }

    private void collectStmt(Film film, String sqlQuery, KeyHolder keyHolder) {
        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sqlQuery, new String[]{"FILM_ID"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, Date.valueOf(film.getReleaseDate()));
            stmt.setLong(4, film.getDuration());
            stmt.setInt(5, film.getMpa().getId());
            return stmt;
        }, keyHolder);
    }

    private Film collectFilm(ResultSet rs) throws SQLException {
        int id = rs.getInt("FILM_ID");
        String name = rs.getString("FILM_NAME");
        String description = rs.getString("FILM_DESCRIPTION");
        LocalDate releaseDate = rs.getDate("FILM_RELEASE_DATE").toLocalDate();
        long duration = rs.getLong("FILM_DURATION");
        int mpaId = rs.getInt("FILM_RATING");
        Mpa mpa = makeRating(mpaId);
        Film res = Film.builder().id(id).name(name).description(description).releaseDate(releaseDate)
                .duration(duration).mpa(mpa).build();
        res.getLikes().addAll(getLikesByFilm(res.getId()));
        res.getGenres().addAll(getGenreByFilm(res.getId()));
        return res;
    }

    private List<Integer> getLikesByFilm(Integer filmId) {
        String sql = "select USER_ID from LIKES where FILM_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> pickUserId(rs), filmId);
    }

    private Integer pickUserId(ResultSet rs) throws SQLException {
        return rs.getInt("USER_ID");
    }

    private List<Genre> getGenreByFilm(Integer filmId) {
        String sql = "select * from FILM_GENRE where FILM_ID = ?";
        return jdbcTemplate
                .query(sql, (rs, rowNum) -> getGenreById(rs.getInt("GENRE_ID")), filmId);
    }

    private void setGenre(Film film) {
        List<Genre> tmp = new ArrayList<>();
        genreRemove(film);
        if (film.getGenres().size() != 0) {
            film.getGenres()
                    .stream()
                    .distinct()
                    .map(Genre::getId)
                    .map(id -> getGenreById(id))
                    .forEach(g -> tmp.add(g));
            film.getGenres().clear();
            film.getGenres().addAll(tmp);
            film.getGenres().stream().forEach(g -> genreInsert(film, g));
        } else {
            film.getGenres().clear();
        }
    }

    private void genreInsert(Film film, Genre genre) {
        String sqlQuery = "insert into FILM_GENRE (FILM_ID, GENRE_ID) values (?, ?)";
        jdbcTemplate.update(sqlQuery, film.getId(), genre.getId());
    }

    private void genreRemove(Film film) {
        String sql = "delete from FILM_GENRE where FILM_ID = ?";
        jdbcTemplate.update(sql, film.getId());
    }

    private Genre makeGenre(Integer id) {
        if (GENRE.containsKey(id)) {
            return Genre.builder()
                    .id(id)
                    .name(GENRE.get(id)).build();
        }
        throw new UserNotFoundException("Genre not found by Id " + id);

    }

    private void setMpaAndGenre(Film film) {
        film.setMpa(makeRating(film.getMpa().getId()));
        if (film.getGenres() != null) {
            film.getGenres()
                    .stream()
                    .map(Genre::getId)
                    .map(id -> getGenreById(id)).forEach(g -> film.getGenres().add(g));
        }
    }

    private Mpa makeRating(Integer id) {
        if (MPA.containsKey(id)) {
            return Mpa.builder().id(id).name(MPA.get(id)).build();
        }
        throw new UserNotFoundException("FilmRating not found by Id: " + id);
    }

}
