package ru.practicum.shareit.item;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class ItemServiceImpl implements ItemService {
    private final Map<Long, Item> items = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong();
    private final UserService userService; // Добавляем UserService

    public ItemServiceImpl(UserService userService) { // Инъекция зависимости
        this.userService = userService;
    }

    @Override
    public ItemDto createItem(Long ownerId, ItemDto itemDto) {
        // Проверяем, существует ли пользователь
        if (!userExists(ownerId)) {
            throw new NoSuchElementException("Пользователь не найден");
        }

        Item item = ItemMapper.toItem(itemDto, ownerId);
        item.setId(idGenerator.incrementAndGet());
        items.put(item.getId(), item);
        return ItemMapper.toItemDto(item);
    }

    @Override
    public ItemDto updateItem(Long id, Long ownerId, ItemDto itemDto) {
        Item existingItem = items.get(id);
        if (existingItem == null || !existingItem.getOwner().equals(ownerId)) {
            throw new NoSuchElementException("Item not found or access denied");
        }
        if (itemDto.getName() != null) {
            existingItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            existingItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            existingItem.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.toItemDto(existingItem);
    }

    @Override
    public ItemDto getItemById(Long id) {
        Item item = items.get(id);
        if (item == null) {
            throw new NoSuchElementException("Item not found");
        }
        return ItemMapper.toItemDto(item);
    }

    @Override
    public List<ItemDto> getItemsByOwner(Long ownerId) {
        return items.values().stream()
                .filter(item -> item.getOwner().equals(ownerId))
                .map(ItemMapper::toItemDto)
                .toList();
    }

    @Override
    public List<ItemDto> searchItems(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }
        return items.values().stream()
                .filter(item -> (item.getName().toLowerCase().contains(text.toLowerCase())
                        || item.getDescription().toLowerCase().contains(text.toLowerCase()))
                        && item.getAvailable())
                .map(ItemMapper::toItemDto)
                .toList();
    }

    /**
     * Проверяет, существует ли пользователь с указанным ID.
     */
    private boolean userExists(Long userId) {
        return userService.getUserById(userId) != null; // getUserById уже бросает исключение, если нет пользователя
    }
}
