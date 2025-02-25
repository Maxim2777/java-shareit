package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.model.User;

@Entity
@Table(name = "items") // Связываем с таблицей items в БД
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Авто-инкрементное поле
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "is_available", nullable = false)
    private Boolean available; // Доступность для аренды

    @ManyToOne
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner; // Связь с владельцем вещи

    @Column(name = "request_id")
    private Long requestId; // ID запроса, если вещь создана по запросу
}