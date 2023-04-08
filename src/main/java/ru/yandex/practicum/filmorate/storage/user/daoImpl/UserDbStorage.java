package ru.yandex.practicum.filmorate.storage.user.daoImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exceptions.UserNotFoundException;
import ru.yandex.practicum.filmorate.model.user.Friendship;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.dao.UserStorage;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static ru.yandex.practicum.filmorate.controllers.validate.UserValidation.validation;
import static ru.yandex.practicum.filmorate.storage.SqlConstants.*;

@Slf4j
@Component
@Primary
@RequiredArgsConstructor
public class UserDbStorage implements UserStorage {
    private final JdbcTemplate jdbcTemplate;

    @Override
    public List<User> usersList() {
        List<User> allUsersList = new ArrayList<>();
        String sql = "select * from USERS";
        allUsersList = jdbcTemplate.query(sql, (rs, rowNum) -> collectUser(rs));
        log.debug("All Users List collected !");
        return allUsersList;
    }

    @Override
    public User create(User user) {
        KeyHolder userKey = new GeneratedKeyHolder();
        User finalUser = prepareUser(user);
        jdbcTemplate.update(con -> {
            PreparedStatement stmt = con.prepareStatement(CREATE_USER, new String[]{"USER_ID"});
            stmt.setString(1, finalUser.getEmail());
            stmt.setString(2, finalUser.getLogin());
            stmt.setString(3, finalUser.getName());
            stmt.setDate(4, Date.valueOf(finalUser.getBirthday()));
            return stmt;
        }, userKey);
        user.setId(userKey.getKey().intValue());
        log.debug("User with ID - " + user.getId() + " collected !");
        return user;
    }

    @Override
    public User findUserById(Integer id) {
        String sql = "select * from USERS where USER_ID = ?";
        return (jdbcTemplate.query(sql, (rs, rowNum) -> collectUser(rs), id))
                .stream().findFirst().orElseThrow(() -> {
                    throw new UserNotFoundException("User with ID- " + id + " not found !");
                });
    }

    @Override
    public User update(User user) {
        findUserById(user.getId());
        jdbcTemplate.update(UPDATE_USER, user.getEmail(), user.getLogin(),
                user.getName(), user.getBirthday(), user.getId());
        log.debug("User with ID - " + user.getId() + " updated !");
        return findUserById(user.getId());
    }

    @Override
    public void addFriendById(Integer userId, Integer friendId) {
        findUserById(friendId);
        String sql = "insert into FRIENDSHIP (USER_ID, FRIEND_ID, CONFIRM) values (?, ?, ?)";
        if (getFriendshipList(friendId).contains(makeFriendship(userId, false))) {
            jdbcTemplate.update(UPDATE_FRIENDSHIP, true, friendId, userId);
            jdbcTemplate.update(sql, userId, friendId, true);
            log.debug("User with ID - " + userId + " add friend !");
        } else {
            jdbcTemplate.update(sql, userId, friendId, false);
            log.debug("User with ID - " + userId + " add try to friend !");
        }
    }

    @Override
    public void deleteFriendById(Integer userId, Integer friendId) {
        findUserById(friendId);
        String sql = "delete from FRIENDSHIP where USER_ID = ? and FRIEND_ID = ?";
        if ((getFriendshipList(userId).contains(makeFriendship(friendId, true))) ||
                (getFriendshipList(userId).contains(makeFriendship(friendId, false)))) {
            jdbcTemplate.update(sql, userId, friendId);
            jdbcTemplate.update(sql, friendId, userId);
            log.debug("User with ID - " + userId + " drop friendship !");
        } else {
            throw new UserNotFoundException("Friendship with user with ID- " + friendId + " not found !");
        }
    }

    private User collectUser(ResultSet rs) throws SQLException {
        int id = rs.getInt("USER_ID");
        String email = rs.getString("USERS_EMAIL");
        String login = rs.getString("USERS_LOGIN");
        String name = rs.getString("USERS_NAME");
        LocalDate birthday = rs.getDate("USERS_BIRTHDAY").toLocalDate();
        User resColl = User.builder().id(id).email(email).login(login).name(name).birthday(birthday).build();
//        List<Friendship> friendshipList = getFriendshipList(id);
        resColl.getFriends().addAll(getFriendshipList(id));
        return resColl;
    }

    private Friendship makeFriendship(Integer id, Boolean b) {
        return Friendship.builder()
                .friendId(id).beFriend(b).build();
    }

    private User prepareUser(User user) {
        if (validation(user)) {
            if (user.getName().isEmpty()) {
                user.setName(user.getLogin());
            }
        }
        return user;
    }

    private List<Friendship> getFriendshipList(Integer id) {
        String sql = "select * from FRIENDSHIP where USER_ID = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> makeFriendship(rs.getInt("FRIEND_ID"),
                rs.getBoolean("CONFIRM")), id);
    }

}
