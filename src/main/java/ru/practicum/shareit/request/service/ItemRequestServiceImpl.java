package ru.practicum.shareit.request.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.request.ItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.mapper.ItemRequestMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {
    private final Map<Long, ItemRequest> requests = new HashMap<>();
    private long nextId = 1;
    private final UserService userService;

    public ItemRequestServiceImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ItemRequestDto createRequest(Long userId, ItemRequestDto itemRequestDto) {
        // Получаем UserDto и конвертируем в User
        User user = UserMapper.toUser(userService.getUser(userId));

        ItemRequest request = ItemRequest.builder()
                .id(nextId++)
                .description(itemRequestDto.getDescription())
                .requestor(user)
                .created(LocalDateTime.now())
                .build();

        requests.put(request.getId(), request);
        return ItemRequestMapper.toItemRequestDto(request);
    }

    @Override
    public List<ItemRequestDto> getUserRequests(Long userId) {
        return requests.values().stream()
                .filter(req -> req.getRequestor().getId().equals(userId))
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Long userId, int from, int size) {
        return requests.values().stream()
                .filter(req -> !req.getRequestor().getId().equals(userId))
                .skip(from)
                .limit(size)
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getRequestById(Long userId, Long requestId) {
        ItemRequest request = requests.get(requestId);
        if (request == null) {
            throw new NoSuchElementException("Request not found");
        }
        return ItemRequestMapper.toItemRequestDto(request);
    }
}
