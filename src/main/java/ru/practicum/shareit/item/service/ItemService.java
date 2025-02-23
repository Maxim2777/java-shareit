package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(Long ownerId, ItemDto itemDto, Long requestId);

    ItemDto getItem(Long id, Long userId);

    ItemDto updateItem(Long ownerId, Long itemId, ItemDto itemDto);

    List<ItemDto> getUserItems(Long ownerId);

    List<ItemDto> searchItems(String text);

    // 🔹 Получение вещей по requestId
    List<ItemDto> getItemsByRequestId(Long requestId);
}