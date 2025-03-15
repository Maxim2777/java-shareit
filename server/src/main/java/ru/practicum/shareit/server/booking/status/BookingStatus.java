package ru.practicum.shareit.server.booking.status;

public enum BookingStatus {
    WAITING,    // Ожидает подтверждения владельцем
    APPROVED,   // Подтверждено владельцем
    REJECTED,   // Отклонено владельцем
    CANCELED    // Отменено пользователем
}