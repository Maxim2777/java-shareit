package ru.practicum.shareit.booking;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class Booking {
    private Long id; // Уникальный идентификатор бронирования
    private LocalDateTime start; // Дата и время начала бронирования
    private LocalDateTime end; // Дата и время окончания бронирования
    private Long item; // Идентификатор вещи
    private Long booker; // Идентификатор пользователя, осуществляющего бронирование
    private Status status; // Статус бронирования

    public enum Status {
        WAITING, APPROVED, REJECTED, CANCELED
    }
}
