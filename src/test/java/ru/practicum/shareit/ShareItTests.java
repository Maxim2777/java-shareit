package ru.practicum.shareit;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ShareItTests {

	@Autowired
	private UserService userService;

	@Autowired
	private ItemService itemService;

	@Autowired
	private BookingService bookingService;

	// Генерация случайного email с использованием UUID
	private String generateRandomEmail() {
		return UUID.randomUUID().toString().substring(0, 8) + "@example.com";
	}

	// Тесты для UserService
	@Test
	void createUserShouldReturnSavedUserDto() {
		UserDto userDto = new UserDto(null, "Test User", generateRandomEmail());
		UserDto savedUser = userService.createUser(userDto);

		assertNotNull(savedUser.getId());
		assertEquals("Test User", savedUser.getName());
		assertTrue(savedUser.getEmail().endsWith("@example.com"));
	}

	@Test
	void getUserByIdShouldThrowExceptionIfUserNotFound() {
		assertThrows(NoSuchElementException.class, () -> userService.getUser(999L));
	}

	@Test
	void updateUserShouldModifyExistingUser() {
		UserDto userDto = userService.createUser(new UserDto(null, "Old Name", generateRandomEmail()));
		UserDto updatedUser = userService.updateUser(userDto.getId(), new UserDto(null, "New Name", null));

		assertEquals("New Name", updatedUser.getName());
		assertTrue(updatedUser.getEmail().endsWith("@example.com")); // Email не должен измениться
	}

	// Тесты для ItemService
	@Test
	void addItemShouldReturnSavedItemDto() {
		UserDto user = userService.createUser(new UserDto(null, "User 1", generateRandomEmail()));
		ItemDto itemDto = new ItemDto(null, "Drill", "Powerful drill", true, null);

		ItemDto savedItem = itemService.addItem(user.getId(), itemDto);

		assertNotNull(savedItem.getId());
		assertEquals("Drill", savedItem.getName());
	}

	@Test
	void addItemShouldThrowExceptionIfUserNotFound() {
		ItemDto itemDto = new ItemDto(null, "Drill", "Powerful drill", true, null);
		assertThrows(NoSuchElementException.class, () -> itemService.addItem(999L, itemDto));
	}

	@Test
	void getItemByIdShouldThrowExceptionIfNotFound() {
		assertThrows(NoSuchElementException.class, () -> itemService.getItem(999L));
	}

	// Тесты для BookingService
	@Test
	void createBookingShouldReturnSavedBookingDto() {
		UserDto user = userService.createUser(new UserDto(null, "User 1", generateRandomEmail()));
		ItemDto item = itemService.addItem(user.getId(),
				new ItemDto(null, "Drill", "Powerful drill", true, null));

		BookingDto bookingDto = new BookingDto(null, LocalDateTime.now().plusDays(1),
				LocalDateTime.now().plusDays(2), item, null, BookingStatus.WAITING);
		BookingDto savedBooking = bookingService.createBooking(user.getId(), bookingDto);

		assertNotNull(savedBooking.getId());
		assertEquals(BookingStatus.WAITING, savedBooking.getStatus());
	}

	@Test
	void createBookingShouldThrowExceptionIfItemNotAvailable() {
		UserDto user = userService.createUser(new UserDto(null, "User 1", generateRandomEmail()));
		ItemDto item = itemService.addItem(user.getId(),
				new ItemDto(null, "Drill", "Powerful drill", false, null));

		BookingDto bookingDto = new BookingDto(null, LocalDateTime.now().plusDays(1),
				LocalDateTime.now().plusDays(2), item, null, BookingStatus.WAITING);

		assertThrows(IllegalStateException.class, () -> bookingService.createBooking(user.getId(), bookingDto));
	}

	@Test
	void approveBookingShouldChangeStatusToApproved() {
		UserDto user = userService.createUser(new UserDto(null, "User 1", generateRandomEmail()));
		ItemDto item = itemService.addItem(user.getId(),
				new ItemDto(null, "Drill", "Powerful drill", true, null));

		BookingDto bookingDto = bookingService.createBooking(user.getId(), new BookingDto(null,
				LocalDateTime.now().plusDays(1), LocalDateTime.now().plusDays(2),
				item, null, BookingStatus.WAITING));

		BookingDto approvedBooking = bookingService.approveBooking(user.getId(), bookingDto.getId(), true);

		assertEquals(BookingStatus.APPROVED, approvedBooking.getStatus());
	}
}