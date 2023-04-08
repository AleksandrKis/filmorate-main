package ru.yandex.practicum.filmorate.model.user;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@Builder
@EqualsAndHashCode
public class Friendship {
    private int friendId;
    private boolean beFriend;
}
