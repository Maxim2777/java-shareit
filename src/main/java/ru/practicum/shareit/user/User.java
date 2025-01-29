package ru.practicum.shareit.user;

import lombok.Data;

@Data
public class User {
    private Long id; // Уникальный идентификатор пользователя
    private String name; // Имя пользователя
    private String email; // Электронная почта пользователя
}
