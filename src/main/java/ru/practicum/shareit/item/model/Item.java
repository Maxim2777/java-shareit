package ru.practicum.shareit.item.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available; // Доступность для аренды
    private Long ownerId; // Владелец вещи
    private Long requestId; // Если создано по запросу
}
