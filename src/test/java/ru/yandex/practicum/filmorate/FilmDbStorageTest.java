package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.film.Film;
import ru.yandex.practicum.filmorate.model.film.Genre;
import ru.yandex.practicum.filmorate.model.film.Mpa;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.film.dao.FilmStorage;
import ru.yandex.practicum.filmorate.storage.user.dao.UserStorage;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static ru.yandex.practicum.filmorate.utilities.Constants.MPA;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final UserStorage userStorage;
    private final FilmStorage filmStorage;
    private final JdbcTemplate jdbcTemplate;
    private Film testFilm1, testFilm2, testFilm3;
    private User testUser1, testUser2, testUser3;

    @BeforeEach
    public void initTests() {
        testUser1 = userStorage.create(User.builder()
                .email("testUser1@mail.ru")
                .login("loginTestUser1")
                .name("loginTestUser1")
                .birthday(LocalDate.of(2001, 01, 01)).build());
        testUser2 = userStorage.create(User.builder()
                .email("testUser2@mail.ru")
                .login("loginTestUser2")
                .name("loginTestUser2")
                .birthday(LocalDate.of(2002, 02, 02)).build());
        testUser3 = userStorage.create(User.builder()
                .email("testUser3@mail.ru")
                .login("loginTestUser3")
                .name("loginTestUser3")
                .birthday(LocalDate.of(2003, 03, 03)).build());
        testFilm1 = filmStorage.create(Film.builder()
                .name("testFilm1")
                .description("The film is about testFilm1.")
                .releaseDate(LocalDate.of(2001, 01, 01))
                .duration(111)
                .mpa(Mpa.builder().id(1).name(MPA.get(1)).build()).build());
        testFilm2 = filmStorage.create(Film.builder()
                .name("testFilm2")
                .description("The film is about testFilm2.")
                .releaseDate(LocalDate.of(2002, 02, 02))
                .duration(222)
                .mpa(Mpa.builder().id(2).name(MPA.get(2)).build()).build());
        testFilm3 = filmStorage.create(Film.builder()
                .name("testFilm3")
                .description("The film is about testFilm3.")
                .releaseDate(LocalDate.of(2003, 03, 03))
                .duration(333)
                .mpa(Mpa.builder().id(3).name(MPA.get(3)).build()).build());
        filmStorage.addLikeFilmById(testFilm3.getId(), testUser3.getId());
        testFilm3.getLikes().add(testUser3.getId());
    }

    @AfterEach
    public void reset() {
        jdbcTemplate.execute("DELETE FROM LIKES;");
        jdbcTemplate.execute("DELETE FROM FILMS");
        jdbcTemplate.execute("DELETE FROM USERS");
        jdbcTemplate.execute("ALTER TABLE FILMS ALTER COLUMN FILM_ID RESTART WITH 1;");
        jdbcTemplate.execute("ALTER TABLE USERS ALTER COLUMN USER_ID RESTART WITH 1;");
    }

    @Test
    public void createFilmTest() {
        Film expectedFilm = filmStorage.findFilmById(1);
        assertEquals(testFilm1, expectedFilm, "Wrong test createFilmTest !");
    }

    @Test
    public void updateFilmTest() {
        Film testUpdateFilm = Film.builder()
                .id(1)
                .name("the Truth is always the same")
                .description("Truth of the world not for everyone.")
                .releaseDate(LocalDate.of(2014, 01, 01))
                .duration(188)
                .mpa(Mpa.builder().id(5).name("NC-17").build()).build();
        filmStorage.update(testUpdateFilm);
        Film expectedFilm = filmStorage.findFilmById(1);
        assertEquals(testUpdateFilm, expectedFilm, "Wrong test updateFilmTest !");

    }

    @Test
    public void findAllAndOneFilmTest() {
        Film expectedFilm1 = filmStorage.findFilmById(1);
        Film expectedFilm2 = filmStorage.findFilmById(2);
        Film expectedFilm3 = filmStorage.findFilmById(3);
        List<Film> expectedListFilms = filmStorage.getAllFilms();
        assertEquals(testFilm1, expectedFilm1, "Wrong search testFilm1");
        assertEquals(testFilm2, expectedFilm2, "Wrong search testFilm2");
        assertEquals(testFilm3, expectedFilm3, "Wrong search testFilm3");
        assertEquals(3, expectedListFilms.size(), "Wrong search allFilms");
    }

    @Test
    public void addLikeToFilmByIdTest() {
        filmStorage.addLikeFilmById(testFilm1.getId(), testUser1.getId());
        Film expectedFilm = filmStorage.findFilmById(testFilm1.getId());
        assertTrue(expectedFilm.getLikes().contains(testUser1.getId()));
    }

    @Test
    public void deleteLikeFromFilmByIdTest() {
        Set<Integer> res = testFilm3.getLikes();
        assertEquals(1, testFilm3.getLikes().size(), "Wrong size Like List");
        filmStorage.delLikeFilmById(3, 3);
        Film expectedFilm = filmStorage.findFilmById(3);
        assertEquals(0, expectedFilm.getLikes().size(), "Wrong size Like List");
    }

    @Test
    public void getPopularFilmListTest() {
        List<Film> expectedPopularFilmsList = filmStorage.getPopularFilmList(1);
        assertEquals(1, expectedPopularFilmsList.size(), "Wrong size PopularFilmList");
        assertTrue(expectedPopularFilmsList.contains(testFilm3), "Wrong Film in PopularFilmList");
    }

    @Test
    public void getAllGenreTest() {
        List<Genre> expectedListGenres = filmStorage.getAllGenre();
        assertEquals(6, expectedListGenres.size());
    }

    @Test
    public void getGenreByIdTest() {
        Genre expectedGenre1 = filmStorage.getGenreById(1);
        Genre expectedGenre2 = filmStorage.getGenreById(2);
        Genre expectedGenre3 = filmStorage.getGenreById(3);
        Genre expectedGenre4 = filmStorage.getGenreById(4);
        Genre expectedGenre5 = filmStorage.getGenreById(5);
        Genre expectedGenre6 = filmStorage.getGenreById(6);
        assertTrue(expectedGenre1.getName().equals("Комедия"));
        assertTrue(expectedGenre2.getName().equals("Драма"));
        assertTrue(expectedGenre3.getName().equals("Мультфильм"));
        assertTrue(expectedGenre4.getName().equals("Триллер"));
        assertTrue(expectedGenre5.getName().equals("Документальный"));
        assertTrue(expectedGenre6.getName().equals("Боевик"));
    }

    @Test
    public void getAllMpaTest() {
        List<Mpa> expectedMpaList = filmStorage.getAllMpa();
        assertEquals(5, expectedMpaList.size());
    }

    @Test
    public void getMpaByIdTest() {
        Mpa expectedMpa1 = filmStorage.getMpaById(1);
        Mpa expectedMpa2 = filmStorage.getMpaById(2);
        Mpa expectedMpa3 = filmStorage.getMpaById(3);
        Mpa expectedMpa4 = filmStorage.getMpaById(4);
        Mpa expectedMpa5 = filmStorage.getMpaById(5);
        assertTrue(expectedMpa1.getName().equals("G"));
        assertTrue(expectedMpa2.getName().equals("PG"));
        assertTrue(expectedMpa3.getName().equals("PG-13"));
        assertTrue(expectedMpa4.getName().equals("R"));
        assertTrue(expectedMpa5.getName().equals("NC-17"));
    }
}
