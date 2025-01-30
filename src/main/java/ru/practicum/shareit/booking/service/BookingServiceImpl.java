package ru.practicum.shareit.booking.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.mapper.BookingMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.service.UserService;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class BookingServiceImpl implements BookingService {
    private final Map<Long, Booking> bookings = new HashMap<>();
    private long nextId = 1;
    private final UserService userService;
    private final ItemService itemService;

    public BookingServiceImpl(UserService userService, ItemService itemService) {
        this.userService = userService;
        this.itemService = itemService;
    }

    @Override
    public BookingDto createBooking(Long userId, BookingDto bookingDto) {
        User booker = UserMapper.toUser(userService.getUser(userId)); // Конвертируем UserDto -> User

        // Сначала получаем ItemDto
        ItemDto itemDto = itemService.getItem(bookingDto.getItem().getId());

        // Затем конвертируем в Item
        Item item = ItemMapper.toItem(itemDto, itemDto.getId());

        if (!item.getAvailable()) {
            throw new IllegalStateException("Item is not available for booking");
        }

        Booking booking = Booking.builder()
                .id(nextId++)
                .start(bookingDto.getStart())
                .end(bookingDto.getEnd())
                .item(item)
                .booker(booker)
                .status(BookingStatus.WAITING)
                .build();

        bookings.put(booking.getId(), booking);
        return BookingMapper.toBookingDto(booking);
    }


    @Override
    public BookingDto approveBooking(Long ownerId, Long bookingId, boolean approved) {
        Booking booking = bookings.get(bookingId);
        if (booking == null) {
            throw new NoSuchElementException("Booking not found");
        }

        if (!booking.getItem().getOwnerId().equals(ownerId)) {
            throw new IllegalStateException("Only owner can approve bookings");
        }

        booking.setStatus(approved ? BookingStatus.APPROVED : BookingStatus.REJECTED);
        return BookingMapper.toBookingDto(booking);
    }


    @Override
    public BookingDto getBooking(Long userId, Long bookingId) {
        Booking booking = bookings.get(bookingId);
        if (booking == null || (!booking.getBooker().getId().equals(userId) && !booking.getItem().getOwnerId().equals(userId))) {
            throw new NoSuchElementException("Booking not found or access denied");
        }
        return BookingMapper.toBookingDto(booking);
    }

    @Override
    public List<BookingDto> getUserBookings(Long userId, String state) {
        return bookings.values().stream()
                .filter(b -> b.getBooker().getId().equals(userId))
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingDto> getOwnerBookings(Long ownerId, String state) {
        return bookings.values().stream()
                .filter(b -> b.getItem().getOwnerId().equals(ownerId))
                .map(BookingMapper::toBookingDto)
                .collect(Collectors.toList());
    }
}
