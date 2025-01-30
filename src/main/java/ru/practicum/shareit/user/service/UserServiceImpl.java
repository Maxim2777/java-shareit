package ru.practicum.shareit.user.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.mapper.UserMapper;

import java.util.*;

@Service
public class UserServiceImpl implements UserService {
    private final Map<Long, User> users = new HashMap<>();
    private long nextId = 1;

    @Override
    public UserDto createUser(UserDto userDto) {
        // Проверяем, не занят ли email
        if (emailExists(userDto.getEmail())) {
            throw new IllegalStateException("Email уже используется другим пользователем");
        }

        User user = UserMapper.toUser(userDto);
        user.setId(nextId++);
        users.put(user.getId(), user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto getUser(Long id) {
        User user = Optional.ofNullable(users.get(id))
                .orElseThrow(() -> new NoSuchElementException("User not found"));
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        List<UserDto> result = new ArrayList<>();
        for (User user : users.values()) {
            result.add(UserMapper.toUserDto(user));
        }
        return result;
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        User existingUser = users.get(id);
        if (existingUser == null) {
            throw new NoSuchElementException("User not found");
        }

        // Если email меняется, проверяем его уникальность
        if (userDto.getEmail() != null && !userDto.getEmail().equals(existingUser.getEmail())) {
            if (emailExists(userDto.getEmail())) {
                throw new IllegalStateException("Email уже используется другим пользователем");
            }
            existingUser.setEmail(userDto.getEmail());
        }

        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }

        return UserMapper.toUserDto(existingUser);
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    // Проверка, существует ли уже email у другого пользователя
    private boolean emailExists(String email) {
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }
}

