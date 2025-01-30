package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto addItem(Long ownerId, ItemDto itemDto);
    ItemDto getItem(Long id);
    ItemDto updateItem(Long id, ItemDto itemDto);
    List<ItemDto> getUserItems(Long ownerId);
    List<ItemDto> searchItems(String text);
}
