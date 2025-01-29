package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto); // Создание пользователя
    UserDto updateUser(Long id, UserDto userDto); // Обновление данных пользователя
    UserDto getUserById(Long id); // Получение пользователя по ID
    List<UserDto> getAllUsers(); // Получение списка всех пользователей
    void deleteUser(Long id); // Удаление пользователя
}
