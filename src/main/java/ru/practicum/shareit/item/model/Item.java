package ru.practicum.shareit.item.model;

import lombok.Data;

@Data
public class Item {
    private Long id; // Уникальный идентификатор вещи
    private String name; // Название вещи
    private String description; // Описание вещи
    private Boolean available; // Доступна ли вещь для аренды
    private Long owner; // Владелец вещи (идентификатор пользователя)
    private Long request; // Ссылка на запрос вещи, если она создана по запросу
}
