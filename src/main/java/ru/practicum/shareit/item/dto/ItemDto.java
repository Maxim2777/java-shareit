package ru.practicum.shareit.item.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.practicum.shareit.booking.dto.BookingShortDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemDto {
    private Long id;

    @NotBlank(message = "Название вещи не может быть пустым")
    private String name;

    @NotBlank(message = "Описание вещи не может быть пустым")
    private String description;

    @NotNull(message = "Доступность вещи должна быть указана")
    private Boolean available;

    private Long requestId;

    private BookingShortDto lastBooking; // 🔹 Последнее бронирование
    private BookingShortDto nextBooking; // 🔹 Следующее бронирование
    private List<CommentDto> comments;   // 🔹 Отзывы пользователей
}