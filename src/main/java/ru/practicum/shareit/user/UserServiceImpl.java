package ru.practicum.shareit.user;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserServiceImpl implements UserService {
    private final Map<Long, User> users = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();

    @Override
    public UserDto createUser(UserDto userDto) {
        checkEmailExists(userDto.getEmail(), null);

        User user = UserMapper.toUser(userDto);
        user.setId(idGenerator.incrementAndGet());
        users.put(user.getId(), user);
        return UserMapper.toUserDto(user);
    }

    @Override
    public UserDto updateUser(Long id, UserDto userDto) {
        if (!users.containsKey(id)) {
            throw new NoSuchElementException("User not found");
        }
        User existingUser = users.get(id);
        if (userDto.getEmail() != null) {
            checkEmailExists(userDto.getEmail(), id); // Проверяем email, кроме текущего пользователя
            existingUser.setEmail(userDto.getEmail());
        }
        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }
        return UserMapper.toUserDto(existingUser);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = users.get(id);
        if (user == null) {
            throw new NoSuchElementException("User not found");
        }
        return UserMapper.toUserDto(user);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return users.values().stream()
                .map(UserMapper::toUserDto)
                .toList();
    }

    @Override
    public void deleteUser(Long id) {
        users.remove(id);
    }

    /**
     * Проверяет, существует ли уже пользователь с таким email.
     * @param email Проверяемый email
     * @param userId ID текущего пользователя (null, если создаём нового)
     */
    private void checkEmailExists(String email, Long userId) {
        boolean emailExists = users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email) && !Objects.equals(user.getId(), userId));

        if (emailExists) {
            throw new IllegalArgumentException("Пользователь с таким email уже существует");
        }
    }
}
