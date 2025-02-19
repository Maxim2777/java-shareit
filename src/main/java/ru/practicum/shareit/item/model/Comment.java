package ru.practicum.shareit.item.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "comments") // 🔹 Связываем с таблицей "comments" в БД
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 🔹 Авто-инкрементное поле
    private Long id;

    @Column(nullable = false)
    private String text; // 🔹 Текст комментария

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item; // 🔹 Вещь, к которой оставили комментарий

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author; // 🔹 Автор комментария

    @Column(nullable = false)
    private LocalDateTime created; // 🔹 Дата и время создания комментария
}
