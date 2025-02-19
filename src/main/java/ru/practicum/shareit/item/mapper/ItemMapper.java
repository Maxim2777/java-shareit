package ru.practicum.shareit.item.mapper;

import ru.practicum.shareit.booking.dto.BookingShortDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public class ItemMapper {

    public static ItemDto toItemDto(Item item) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequestId())
                .build();
    }

    public static ItemDto toItemDto(Item item, Booking lastBooking, Booking nextBooking, List<CommentDto> comments) {
        return ItemDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .requestId(item.getRequestId())
                .lastBooking(lastBooking != null ? BookingShortDto.builder()
                        .id(lastBooking.getId())
                        .bookerId(lastBooking.getBooker().getId())
                        .build() : null)
                .nextBooking(nextBooking != null ? BookingShortDto.builder()
                        .id(nextBooking.getId())
                        .bookerId(nextBooking.getBooker().getId())
                        .build() : null)
                .comments(comments)  // Добавляем комментарии
                .build();
    }

    public static Item toItem(ItemDto itemDto, User owner) {
        return Item.builder()
                .id(itemDto.getId())
                .name(itemDto.getName())
                .description(itemDto.getDescription())
                .available(itemDto.getAvailable())
                .owner(owner)
                .requestId(itemDto.getRequestId())
                .build();
    }
}
