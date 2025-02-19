package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

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

    @NotNull(message = "Item ID cannot be null")
    private Long itemId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private ItemDto item; // ✅ Добавили объект `item`, но он может быть `null`

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long bookerId;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserDto booker; // ✅ Добавили объект `booker`, но он может быть `null`

    private BookingStatus status;

    @FutureOrPresent(message = "Дата начала бронирования должна быть в будущем или настоящем")
    @NotNull(message = "Дата начала бронирования не может быть пустой")
    private LocalDateTime start;

    @Future(message = "Дата окончания бронирования должна быть в будущем")
    @NotNull(message = "Дата окончания бронирования не может быть пустой")
    private LocalDateTime end;
}