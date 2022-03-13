package com.example.checkoutsystem.service;

import com.example.checkoutsystem.model.Item;
import com.example.checkoutsystem.model.Order;
import com.example.checkoutsystem.repository.ItemRepository;
import com.example.checkoutsystem.service.exceptions.EmptyCheckoutListException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class OrderServiceTest {

    @MockBean
    private ItemRepository itemRepository;

    private OrderService orderService;

    @BeforeAll
    public void setup() {
        orderService = new OrderServiceImpl(itemRepository);
    }

    @Test
    public void shouldFetchItemsGroupedByQuantity() {
        // Given
        List<String> items = new ArrayList<>(Arrays.asList("0001", "0001"));
        Item itemWaterBottle = new Item("0001", "Water Bottle", new BigDecimal("24.95"));

        when(itemRepository.findByCode(any())).thenReturn(itemWaterBottle);

        // When
        Map<Item, Long> groupedItems = orderService.fetchItemsGroupedByQuantity(items);

        // Then
        Map.Entry<Item, Long> actualItem = groupedItems.entrySet().iterator().next();
        assertEquals(1, groupedItems.size());
        assertEquals(2L, actualItem.getValue());
        assertEquals("0001", actualItem.getKey().getCode());
        assertEquals("Water Bottle", actualItem.getKey().getName());
        assertEquals(new BigDecimal("24.95"), actualItem.getKey().getPrice());
    }

    @Test
    public void shouldThrowExceptionOnFetchItemsGroupedByQuantity() {
        // Given
        List<String> items = new ArrayList<>(Arrays.asList("0001", "0001"));
        String expectedMessage = "Invalid item code.";

        when(itemRepository.findByCode(any())).thenReturn(null);

        // When
        IllegalArgumentException invalidItemException = assertThrows(IllegalArgumentException.class,
                () -> orderService.fetchItemsGroupedByQuantity(items));

        // Then
        assertEquals(expectedMessage,invalidItemException.getMessage());

    }

    @Test
    public void shouldUpdateTotalPrice() {
        // Given
        Item itemWaterBottle = new Item("0001", "Hoodie", new BigDecimal("65.00"));
        Map<Item, Long> items = new HashMap<>();
        items.put(itemWaterBottle, 2L);
        Order order = new Order(items);

        // When
        orderService.updateTotalPrice(order);

        // Then
        assertEquals(new BigDecimal("130.00"), order.getTotalPrice());

    }
}
