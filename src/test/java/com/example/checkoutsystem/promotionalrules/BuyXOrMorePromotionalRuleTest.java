package com.example.checkoutsystem.promotionalrules;

import com.example.checkoutsystem.model.Item;
import com.example.checkoutsystem.model.Order;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BuyXOrMorePromotionalRuleTest {

    private PromotionalRule buyXOrMorePromotionalRule;

    @Test
    public void shouldApplyRule() {

        // Given
        String itemCode = "0001";
        BigDecimal priceOfItem = new BigDecimal("24.95");
        Item itemWaterBottle = new Item(itemCode, "Water Bottle", priceOfItem);
        Map<Item, Long> itemsInOrder = new HashMap<>();
        itemsInOrder.put(itemWaterBottle, 2L);
        Order order = new Order(itemsInOrder);
        order.setTotalPrice(new BigDecimal("49.90"));

        buyXOrMorePromotionalRule = new BuyXOrMorePromotionalRule(itemCode, new BigDecimal("22.99"), 2);

        // When
        buyXOrMorePromotionalRule.applyRule(order);

        // Then
        assertEquals(new BigDecimal("45.98"), order.getTotalPrice());
    }

    @Test
    public void shouldNotApplyRule() {

        // Given
        String itemCode = "0001";
        BigDecimal priceOfItem = new BigDecimal("24.95");
        Item itemWaterBottle = new Item(itemCode, "Water Bottle", priceOfItem);
        Map<Item, Long> itemsInOrder = new HashMap<>();
        itemsInOrder.put(itemWaterBottle, 1L);
        Order order = new Order(itemsInOrder);
        order.setTotalPrice(priceOfItem); // Initial Total Price

        buyXOrMorePromotionalRule = new BuyXOrMorePromotionalRule(itemCode, new BigDecimal("22.99"), 2);

        // When
        buyXOrMorePromotionalRule.applyRule(order);

        // Then
        assertEquals(new BigDecimal("24.95"), order.getTotalPrice());
    }
}
