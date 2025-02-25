package ru.practicum.shareit.booking.model;

import jakarta.persistence.*;
import lombok.*;
import ru.practicum.shareit.booking.status.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Entity
@Table(name = "bookings") // Связываем с таблицей bookings в БД
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Авто-инкрементное поле
    private Long id;

    @Column(name = "start_date", nullable = false)
    private LocalDateTime start;

    @Column(name = "end_date", nullable = false)
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item; // Вещь, которую бронируют

    @ManyToOne
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker; // Пользователь, который бронирует

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BookingStatus status; // Статус бронирования (WAITING, APPROVED, REJECTED, CANCELED)
}