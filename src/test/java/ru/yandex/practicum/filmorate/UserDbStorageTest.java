package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.dao.UserStorage;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final UserStorage userStorage;
    private final JdbcTemplate jdbcTemplate;
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
    }

    @AfterEach
    public void reset() {
        jdbcTemplate.execute("DELETE FROM FRIENDSHIP");
        jdbcTemplate.execute("DELETE FROM USERS");
        jdbcTemplate.execute("ALTER TABLE USERS ALTER COLUMN USER_ID RESTART WITH 1;");
    }

    @Test
    public void createUserTest() {
        User expectedUser = userStorage.findUserById(1);
        assertEquals(testUser1, expectedUser, "Wrong test createUserTest !");
    }

    @Test
    public void updateUserTest() {
        User testUpdateUser = User.builder()
                .id(1)
                .email("testUpdateUser@mail.ru")
                .login("testUpdateUser")
                .name("testUpdateUser")
                .birthday(LocalDate.of(2007, 07, 07)).build();
        userStorage.update(testUpdateUser);
        User expectedUser = userStorage.findUserById(1);
        assertEquals(testUpdateUser, expectedUser, "Wrong test updateUserTest !");
    }

    @Test
    public void findAllAndOneUserTest() {
        User expectedUser1 = userStorage.findUserById(1);
        User expectedUser2 = userStorage.findUserById(2);
        User expectedUser3 = userStorage.findUserById(3);
        List<User> expectedListUsers = userStorage.usersList();
        assertEquals(testUser1, expectedUser1, "Wrong test findUserTest !");
        assertEquals(testUser2, expectedUser2, "Wrong test findUserTest !");
        assertEquals(testUser3, expectedUser3, "Wrong test findUserTest !");
        assertEquals(3, expectedListUsers.size(), "Wrong test findAllUsersTest!");
    }

    @Test
    public void addFriendByIdTest() {
        assertEquals(0, testUser1.getFriends().size());
        userStorage.addFriendById(testUser1.getId(), testUser2.getId());
        User expectedUser = userStorage.findUserById(testUser1.getId());
        assertEquals(1, expectedUser.getFriends().size());
    }

    @Test
    public void deleteFriendByIdTest() {
        assertEquals(0, testUser1.getFriends().size());
        userStorage.addFriendById(testUser1.getId(), testUser2.getId());
        User expectedUser = userStorage.findUserById(testUser1.getId());
        assertEquals(1, expectedUser.getFriends().size());
        userStorage.deleteFriendById(expectedUser.getId(), testUser2.getId());
        User expectedUser2 = userStorage.findUserById(testUser1.getId());
        assertEquals(0, expectedUser2.getFriends().size());
    }

    @Test
    public void getAllUsersTest() {
        List<User> allUsersList = userStorage.usersList();
        assertEquals(3, allUsersList.size());
        assertTrue(allUsersList.contains(testUser1));
        assertTrue(allUsersList.contains(testUser2));
        assertTrue(allUsersList.contains(testUser3));
    }

}
