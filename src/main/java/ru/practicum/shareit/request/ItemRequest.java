package ru.practicum.shareit.request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ItemRequest {
    private Long id; // Уникальный идентификатор запроса
    private String description; // Описание требуемой вещи
    private Long requestor; // Идентификатор пользователя, создавшего запрос
    private LocalDateTime created; // Дата и время создания запроса
}
