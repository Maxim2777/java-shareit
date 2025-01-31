package ru.practicum.shareit.item.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ItemServiceImpl implements ItemService {
    private final Map<Long, Item> items = new HashMap<>();
    private long nextId = 1;
    private final UserService userService; // Добавили userService

    public ItemServiceImpl(UserService userService) { // Внедрение через конструктор
        this.userService = userService;
    }

    @Override
    public ItemDto addItem(Long ownerId, ItemDto itemDto) {
        // Проверяем, существует ли пользователь
        userService.getUser(ownerId); // Если нет, выбросится NoSuchElementException

        Item item = ItemMapper.toItem(itemDto, ownerId);
        item.setId(nextId++);
        items.put(item.getId(), item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto getItem(Long id) {
        Item item = Optional.ofNullable(items.get(id))
                .orElseThrow(() -> new NoSuchElementException("Item not found"));
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(Long ownerId, Long itemId, ItemDto itemDto) {
        Item item = items.get(itemId);
        if (item == null) {
            throw new NoSuchElementException("Item not found");
        }

        // Проверяем, что обновляет именно владелец вещи
        if (!item.getOwnerId().equals(ownerId)) {
            throw new NoSuchElementException("User is not the owner of this item");
        }

        // Обновляем только переданные параметры
        if (itemDto.getName() != null) item.setName(itemDto.getName());
        if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());

        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getUserItems(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwnerId().equals(ownerId))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }

        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(text.toLowerCase())) &&
                        item.getAvailable())
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }
}
