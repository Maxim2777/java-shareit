package ru.practicum.shareit.item;

import ru.practicum.shareit.item.dto.ItemDto;

import java.util.List;

public interface ItemService {
    ItemDto createItem(Long ownerId, ItemDto itemDto); // Создание вещи
    ItemDto updateItem(Long id, Long ownerId, ItemDto itemDto); // Обновление вещи
    ItemDto getItemById(Long id); // Получение вещи по ID
    List<ItemDto> getItemsByOwner(Long ownerId); // Получение всех вещей владельца
    List<ItemDto> searchItems(String text); // Поиск вещей по тексту
}
