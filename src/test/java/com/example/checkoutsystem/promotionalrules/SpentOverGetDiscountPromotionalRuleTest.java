package com.example.checkoutsystem.promotionalrules;

import com.example.checkoutsystem.model.Item;
import com.example.checkoutsystem.model.Order;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SpentOverGetDiscountPromotionalRuleTest {

    private PromotionalRule spentOverGetDiscountPromotionalRule;

    @Test
    public void shouldApplyRule() {

        // Given
        String itemCode = "0002";
        Item itemWaterBottle = new Item(itemCode, "Hoodie", new BigDecimal("65.00"));
        spentOverGetDiscountPromotionalRule = new SpentOverGetDiscountPromotionalRule(new BigDecimal("75"), 10D);
        Map<Item, Long> itemsInOrder = new HashMap<>();
        itemsInOrder.put(itemWaterBottle, 2L);
        Order order = new Order(itemsInOrder);
        order.setTotalPrice(new BigDecimal(130));

        // When
        spentOverGetDiscountPromotionalRule.applyRule(order);

        // Then
        assertEquals(new BigDecimal("117.00"), order.getTotalPrice());
    }

    @Test
    public void shouldNotApplyRule() {

        // Given
        String itemCode = "0002";
        Item itemHoodie = new Item(itemCode, "Hoodie", new BigDecimal("65.00"));
        spentOverGetDiscountPromotionalRule = new SpentOverGetDiscountPromotionalRule(new BigDecimal("75"), 10D);
        Map<Item, Long> itemsInOrder = new HashMap<>();
        itemsInOrder.put(itemHoodie, 1L);
        Order order = new Order(itemsInOrder);
        order.setTotalPrice(new BigDecimal(65));

        // When
        spentOverGetDiscountPromotionalRule.applyRule(order);

        // Then
        assertEquals(new BigDecimal("65"), order.getTotalPrice());
    }
}
