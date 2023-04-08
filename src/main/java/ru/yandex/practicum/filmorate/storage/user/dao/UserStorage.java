package ru.yandex.practicum.filmorate.storage.user.dao;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.model.user.User;

import javax.validation.Valid;
import java.util.List;

public interface UserStorage {
    @PostMapping
    User create(@Valid @RequestBody User user);

    @PutMapping
    User update(@Valid @RequestBody User user);

    User findUserById(Integer id);

    void addFriendById(Integer userId, Integer friendId);

    void deleteFriendById(Integer userId, Integer friendId);

    @GetMapping
    List<User> usersList();

}
