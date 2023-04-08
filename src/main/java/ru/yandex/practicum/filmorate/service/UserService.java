package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.user.User;
import ru.yandex.practicum.filmorate.storage.user.dao.UserStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserStorage userStorage;

    public User create(User user) {
        return userStorage.create(user);
    }

    public User update(User user) {
        return userStorage.update(user);
    }

    public List<User> usersList() {
        return userStorage.usersList();
    }

    public User findUserById(Integer id) {
        return userStorage.findUserById(id);
    }

    public void addFriendById(Integer userId, Integer friendId) {
        userStorage.addFriendById(userId, friendId);
    }

    public void deleteFriendById(Integer userId, Integer friendId) {
        userStorage.deleteFriendById(userId, friendId);
    }

    public List<User> getFriendsListById(Integer userId) {
        User user = findUserById(userId);
        List<User> friendList = new ArrayList<>();
        user.getFriends()
                .stream()
                .forEach(i -> friendList.add(findUserById(i.getFriendId())));
        return friendList;
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        return getFriendsListById(userId)
                .stream()
                .filter(f -> getFriendsListById(otherId).contains(f))
                .collect(Collectors.toList());
    }

}

