package ru.practicum.shareit.server.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import ru.practicum.shareit.server.item.dto.ItemDto;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@JsonTest
class ItemDtoJsonTest {

    private ObjectMapper objectMapper;
    private Validator validator;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldSerializeItemDto() throws Exception {
        // Создаем объект ItemDto
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Drill");
        itemDto.setDescription("Powerful drill");
        itemDto.setAvailable(true);
        itemDto.setRequestId(10L);

        // Сериализация в JSON
        String json = objectMapper.writeValueAsString(itemDto);

        // Проверка
        assertThat(json).contains("\"id\":1");
        assertThat(json).contains("\"name\":\"Drill\"");
        assertThat(json).contains("\"description\":\"Powerful drill\"");
        assertThat(json).contains("\"available\":true");
        assertThat(json).contains("\"requestId\":10");
    }

    @Test
    void shouldDeserializeItemDto() throws Exception {
        // JSON-строка
        String json = "{"
                + "\"id\":1,"
                + "\"name\":\"Drill\","
                + "\"description\":\"Powerful drill\","
                + "\"available\":true,"
                + "\"requestId\":10"
                + "}";

        // Десериализация JSON
        ItemDto itemDto = objectMapper.readValue(json, ItemDto.class);

        // Проверка
        assertThat(itemDto.getId()).isEqualTo(1L);
        assertThat(itemDto.getName()).isEqualTo("Drill");
        assertThat(itemDto.getDescription()).isEqualTo("Powerful drill");
        assertThat(itemDto.getAvailable()).isTrue();
        assertThat(itemDto.getRequestId()).isEqualTo(10L);
    }

    @Test
    void shouldFailValidationIfNameIsBlank() {
        // Создаем объект с пустым именем
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("");
        itemDto.setDescription("Powerful drill");
        itemDto.setAvailable(true);

        // Проверяем валидацию
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);

        // Ожидаем ошибку
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Название вещи не может быть пустым");
    }

    @Test
    void shouldFailValidationIfDescriptionIsBlank() {
        // Создаем объект с пустым описанием
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Drill");
        itemDto.setDescription("");
        itemDto.setAvailable(true);

        // Проверяем валидацию
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);

        // Ожидаем ошибку
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Описание вещи не может быть пустым");
    }

    @Test
    void shouldFailValidationIfAvailabilityIsNull() {
        // Создаем объект без флага доступности
        ItemDto itemDto = new ItemDto();
        itemDto.setId(1L);
        itemDto.setName("Drill");
        itemDto.setDescription("Powerful drill");
        itemDto.setAvailable(null); // Доступность должна быть указана

        // Проверяем валидацию
        Set<ConstraintViolation<ItemDto>> violations = validator.validate(itemDto);

        // Ожидаем ошибку
        assertThat(violations).hasSize(1);
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Доступность вещи должна быть указана");
    }
}
