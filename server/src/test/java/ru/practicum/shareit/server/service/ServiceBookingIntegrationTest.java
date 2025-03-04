package ru.practicum.shareit.server.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.server.booking.dto.BookingDto;
import ru.practicum.shareit.server.booking.service.BookingService;
import ru.practicum.shareit.server.booking.status.BookingStatus;
import ru.practicum.shareit.server.item.dto.ItemDto;
import ru.practicum.shareit.server.item.service.ItemService;
import ru.practicum.shareit.server.user.dto.UserDto;
import ru.practicum.shareit.server.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class ServiceBookingIntegrationTest {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private UserService userService;

    @Autowired
    private ItemService itemService;

    private UserDto owner;
    private UserDto booker;
    private ItemDto item;
    private BookingDto bookingDto;

    @BeforeEach
    void setUp() {
        // Создаем пользователя-владельца и пользователя-бронирующего
        owner = userService.createUser(new UserDto(null, "Owner", "owner@mail.com"));
        booker = userService.createUser(new UserDto(null, "Booker", "booker@mail.com"));

        // Создаем вещь
        ItemDto itemDto = new ItemDto();
        itemDto.setName("Drill");
        itemDto.setDescription("Powerful drill");
        itemDto.setAvailable(true);
        item = itemService.addItem(owner.getId(), itemDto);

        // Создаем бронирование
        bookingDto = new BookingDto();
        bookingDto.setItemId(item.getId());
        bookingDto.setStart(LocalDateTime.now().plusDays(1));
        bookingDto.setEnd(LocalDateTime.now().plusDays(2));
    }

    @Test
    void createBooking_shouldSaveAndReturnBooking() {
        // Создание бронирования
        BookingDto createdBooking = bookingService.createBooking(booker.getId(), bookingDto);

        // Проверка
        assertThat(createdBooking).isNotNull();
        assertThat(createdBooking.getId()).isNotNull();
        assertThat(createdBooking.getItem().getId()).isEqualTo(item.getId());
        assertThat(createdBooking.getStatus()).isEqualTo(BookingStatus.WAITING);
    }

    @Test
    void createBooking_shouldFailForUnavailableItem() {
        // Делаем вещь недоступной
        item.setAvailable(false);
        itemService.updateItem(owner.getId(), item.getId(), item);

        // Проверка исключения
        assertThatThrownBy(() -> bookingService.createBooking(booker.getId(), bookingDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Item is not available for booking");
    }

    @Test
    void approveBooking_shouldUpdateStatusToApproved() {
        // Создаем бронирование
        BookingDto createdBooking = bookingService.createBooking(booker.getId(), bookingDto);

        // Подтверждаем бронирование
        BookingDto approvedBooking = bookingService.approveBooking(owner.getId(), createdBooking.getId(), true);

        // Проверка
        assertThat(approvedBooking.getStatus()).isEqualTo(BookingStatus.APPROVED);
    }

    @Test
    void approveBooking_shouldFailIfNotOwner() {
        // Создаем бронирование
        BookingDto createdBooking = bookingService.createBooking(booker.getId(), bookingDto);

        // Проверка исключения при попытке подтверждения бронирования не владельцем
        assertThatThrownBy(() -> bookingService.approveBooking(booker.getId(), createdBooking.getId(), true))
                .isInstanceOf(RuntimeException.class) // `ForbiddenException` внутри
                .hasMessage("Only owner can approve bookings");
    }

    @Test
    void getBooking_shouldReturnBookingForOwnerOrBooker() {
        // Создаем бронирование
        BookingDto createdBooking = bookingService.createBooking(booker.getId(), bookingDto);

        // Проверка для владельца
        BookingDto foundByOwner = bookingService.getBooking(owner.getId(), createdBooking.getId());
        assertThat(foundByOwner).isNotNull();

        // Проверка для бронирующего
        BookingDto foundByBooker = bookingService.getBooking(booker.getId(), createdBooking.getId());
        assertThat(foundByBooker).isNotNull();
    }

    @Test
    void getBooking_shouldThrowExceptionForUnauthorizedUser() {
        // Создаем бронирование
        BookingDto createdBooking = bookingService.createBooking(booker.getId(), bookingDto);

        // Создаем третьего пользователя
        UserDto otherUser = userService.createUser(new UserDto(null, "Other", "other@mail.com"));

        // Проверка, что посторонний пользователь не может получить бронирование
        assertThatThrownBy(() -> bookingService.getBooking(otherUser.getId(), createdBooking.getId()))
                .isInstanceOf(NoSuchElementException.class)
                .hasMessage("Access denied");
    }

    @Test
    void getUserBookings_shouldReturnUserBookings() {
        // Создаем несколько бронирований
        bookingService.createBooking(booker.getId(), bookingDto);
        bookingService.createBooking(booker.getId(), bookingDto);

        // Получаем бронирования пользователя
        List<BookingDto> bookings = bookingService.getUserBookings(booker.getId(), "ALL");

        // Проверка
        assertThat(bookings).hasSize(2);
    }

    @Test
    void getOwnerBookings_shouldReturnOwnerBookings() {
        // Создаем несколько бронирований
        bookingService.createBooking(booker.getId(), bookingDto);
        bookingService.createBooking(booker.getId(), bookingDto);

        // Получаем бронирования владельца
        List<BookingDto> bookings = bookingService.getOwnerBookings(owner.getId(), "ALL");

        // Проверка
        assertThat(bookings).hasSize(2);
    }
}