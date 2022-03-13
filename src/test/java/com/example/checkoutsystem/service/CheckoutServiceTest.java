package com.example.checkoutsystem.service;

import com.example.checkoutsystem.model.Item;
import com.example.checkoutsystem.model.Order;
import com.example.checkoutsystem.promotionalrules.PromotionalRule;
import com.example.checkoutsystem.service.exceptions.EmptyCheckoutListException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CheckoutServiceTest {

    @MockBean
    private OrderService orderService;

    @Captor
    ArgumentCaptor<Order> order;

    private CheckoutService checkoutService;

    List<PromotionalRule> promotionalRules;

    @BeforeAll
    public void setup() {

        // Create the list of rules to be applied
        promotionalRules = new ArrayList<>();
        checkoutService = new CheckoutServiceImpl(promotionalRules, orderService);
    }

    @Test
    public void shouldScanTheListOfItems() {

        // Given
        Item itemWaterBottle = new Item("0001", "Water Bottle", new BigDecimal("24.95"));

        List<String> items = new ArrayList<>(Arrays.asList("0001", "0001", "0002", "0003"));

        Map<Item, Long> expectedItemNumberOfItemsMap = new HashMap<>();
        expectedItemNumberOfItemsMap.put(itemWaterBottle, 2L);
        when(orderService.fetchItemsGroupedByQuantity(items)).thenReturn(expectedItemNumberOfItemsMap);

        // When
        checkoutService.scan(items);

        // Then
        Mockito.verify(orderService).updateTotalPrice(order.capture());
        Order capturedOrder = order.getValue();
        assertNotNull(capturedOrder.getItems());
        assertEquals(capturedOrder.getItems().size(), 1);

        Map.Entry<Item, Long> capturedItem = capturedOrder.getItems().entrySet().iterator().next();

        assertEquals(itemWaterBottle.getCode(), capturedItem.getKey().getCode());
        assertEquals(itemWaterBottle.getPrice(), capturedItem.getKey().getPrice());
        assertEquals(itemWaterBottle.getName(), capturedItem.getKey().getName());
        assertEquals(2L, capturedItem.getValue());
    }

    @Test
    public void shouldTrowExceptionOnScanWhenListOfItemsIsEmpty() {

        // Given
        List<String> items = new ArrayList<>();
        String expectedMessage = "The checkout list is empty.";

        // When
        EmptyCheckoutListException emptyCheckoutListException = assertThrows(EmptyCheckoutListException.class, () -> checkoutService.scan(items));

        // Then
        assertEquals(expectedMessage, emptyCheckoutListException.getErrorMessage());

    }

    @Test
    public void shouldTrowExceptionOnScanWhenListOfItemsIsNull() {

        // Given
        String expectedMessage = "The checkout list is empty.";

        // When
        EmptyCheckoutListException emptyCheckoutListException = assertThrows(EmptyCheckoutListException.class,
                () -> checkoutService.scan(null));

        // Then
        assertEquals(expectedMessage, emptyCheckoutListException.getErrorMessage());
    }
}
