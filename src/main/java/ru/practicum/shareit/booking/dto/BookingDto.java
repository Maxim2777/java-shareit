package ru.practicum.shareit.booking.dto;

import lombok.*;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.User;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingDto {
    private Long id;

    @FutureOrPresent(message = "Дата начала бронирования должна быть в будущем или настоящем")
    @NotNull(message = "Дата начала бронирования не может быть пустой")
    private LocalDateTime start;

    @Future(message = "Дата окончания бронирования должна быть в будущем")
    @NotNull(message = "Дата окончания бронирования не может быть пустой")
    private LocalDateTime end;

    @NotNull(message = "Предмет для бронирования обязателен")
    private ItemDto item;

    private User booker;

    private BookingStatus status;
}

