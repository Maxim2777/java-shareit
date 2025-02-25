package ru.practicum.shareit.item.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    // Найти все вещи, принадлежащие конкретному владельцу
    List<Item> findByOwnerId(Long ownerId);

    // ✅ Исправлено: Добавлены @Param
    @Query("SELECT i FROM Item i LEFT JOIN FETCH i.request WHERE i.request.id = :requestId")
    List<Item> findByRequest_Id(@Param("requestId") Long requestId);

    // ✅ Исправлено: Добавлены @Param
    @Query("SELECT i FROM Item i " +
            "WHERE (UPPER(i.name) LIKE UPPER(CONCAT('%', :text, '%')) " +
            "OR UPPER(i.description) LIKE UPPER(CONCAT('%', :text, '%'))) " +
            "AND i.available = true")
    List<Item> search(@Param("text") String text);
}
