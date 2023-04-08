CREATE TABLE IF NOT EXISTS FILMS (
FILM_ID INTEGER NOT NULL AUTO_INCREMENT,
FILM_NAME CHARACTER VARYING(255) NOT NULL,
FILM_DESCRIPTION CHARACTER VARYING(255),
FILM_RELEASE_DATE DATE,
FILM_DURATION INTEGER,
FILM_RATING INTEGER,
constraint FILM_PK primary key (film_id)
);

CREATE TABLE IF NOT EXISTS USERS (
USERS_EMAIL CHARACTER VARYING(50) NOT NULL,
USERS_LOGIN CHARACTER VARYING(50) NOT NULL,
USERS_NAME CHARACTER VARYING(50),
USERS_BIRTHDAY DATE,
USER_ID INTEGER NOT NULL AUTO_INCREMENT,
constraint USER_PK primary key (user_id)
);

CREATE TABLE IF NOT EXISTS FILM_GENRE (
FILM_ID INTEGER NOT NULL REFERENCES FILMS(FILM_ID),
GENRE_ID INTEGER NOT NULL,
PRIMARY KEY (FILM_ID,GENRE_ID)
);

CREATE TABLE IF NOT EXISTS LIKES (
FILM_ID INTEGER NOT NULL REFERENCES FILMS(FILM_ID),
USER_ID INTEGER NOT NULL REFERENCES USERS(USER_ID),
PRIMARY KEY (FILM_ID,USER_ID)
);

CREATE TABLE IF NOT EXISTS FRIENDSHIP (
FRIENDSHIP_ID INTEGER NOT NULL AUTO_INCREMENT,
USER_ID INTEGER NOT NULL REFERENCES USERS(USER_ID),
FRIEND_ID INTEGER NOT NULL,
CONFIRM BOOLEAN,
PRIMARY KEY (FRIENDSHIP_ID)
);

DELETE FROM FILM_GENRE;
DELETE FROM LIKES;
DELETE FROM FRIENDSHIP;
ALTER TABLE FRIENDSHIP ALTER COLUMN FRIENDSHIP_ID RESTART WITH 1;
DELETE FROM USERS ;
ALTER TABLE USERS ALTER COLUMN USER_ID RESTART WITH 1;
DELETE FROM FILMS ;
ALTER TABLE FILMS ALTER COLUMN FILM_ID RESTART WITH 1;