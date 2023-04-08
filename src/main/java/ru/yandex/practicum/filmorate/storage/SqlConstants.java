package ru.yandex.practicum.filmorate.storage;

public class SqlConstants {
    public static final String CREATE_FILM = "insert into FILMS " +
            "(FILM_NAME, FILM_DESCRIPTION, FILM_RELEASE_DATE, FILM_DURATION, FILM_RATING) " +
            "values (?, ?, ?, ?, ?)";
    public static final String CREATE_USER =
            "insert into USERS (USERS_EMAIL, USERS_LOGIN, USERS_NAME, USERS_BIRTHDAY) values (?, ?, ?, ?)";
    public static final String UPDATE_FILM =
            "update FILMS set FILM_NAME = ?, FILM_DESCRIPTION = ?, FILM_RELEASE_DATE = ?" +
            ", FILM_DURATION = ?, FILM_RATING=? where FILM_ID = ?";
    public static final String UPDATE_USER = "update USERS set USERS_EMAIL = ?, USERS_LOGIN = ?, USERS_NAME = ?" +
            ", USERS_BIRTHDAY = ? where USER_ID = ?";
    public static final String UPDATE_FRIENDSHIP =
            "update FRIENDSHIP set CONFIRM = ? where USER_ID = ? and FRIEND_ID = ?";
    public static final String POPULAR_FILMS = "SELECT f.* ,COUNT(l.FILM_ID) AS LIKES FROM FILMS f LEFT JOIN LIKES l " +
            "ON l.FILM_ID = f.FILM_ID GROUP BY f.FILM_ID ORDER BY f.FILM_ID DESC, LIKES DESC LIMIT ?";
}
