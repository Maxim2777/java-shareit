package ru.practicum.shareit.request.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ItemRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String description; // Описание запроса

    @ManyToOne
    @JoinColumn(name = "requestor_id", nullable = false)
    private User requestor; // Пользователь, создавший запрос

    @Column(nullable = false)
    private LocalDateTime created; // Дата и время создания запроса
}