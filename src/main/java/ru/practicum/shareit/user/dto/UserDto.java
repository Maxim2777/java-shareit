package ru.practicum.shareit.user.dto;

import lombok.*;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;

    @NotBlank(message = "Имя пользователя не должно быть пустым")
    private String name;

    @NotNull
    @Email(message = "Некорректный формат email")
    private String email;
}
