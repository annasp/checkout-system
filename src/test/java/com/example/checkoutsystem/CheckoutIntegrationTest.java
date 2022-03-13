package com.example.checkoutsystem;

import com.example.checkoutsystem.model.Item;
import com.example.checkoutsystem.promotionalrules.BuyXOrMorePromotionalRule;
import com.example.checkoutsystem.promotionalrules.PromotionalRule;
import com.example.checkoutsystem.promotionalrules.SpentOverGetDiscountPromotionalRule;
import com.example.checkoutsystem.repository.ItemRepository;
import com.example.checkoutsystem.service.CheckoutService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CheckoutIntegrationTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private CheckoutService checkoutService;

    private Item itemWaterBottle;

    @BeforeAll
    public void setUp() {

        // Save the products which are listed by the client's e-commerce in DB
        itemWaterBottle = new Item("0001", "Water Bottle", new BigDecimal("24.95"));
        itemRepository.save(itemWaterBottle);

        Item itemHoodie = new Item("0002", "Hoodie", new BigDecimal("65.00"));
        itemRepository.save(itemHoodie);

        Item itemStickerSet = new Item("0003", "Sticker Set", new BigDecimal("3.99"));
        itemRepository.save(itemStickerSet);
    }

    @Test
    public void shouldScanTheItemsAndApplyAllRules() {

        // Given
        String expected_one = "£103.47";
        List<String> items_one = new ArrayList<>(Arrays.asList("0001", "0001", "0002", "0003"));

        String expected_two = "£68.97";
        List<String> items_two = new ArrayList<>(Arrays.asList("0001", "0001", "0001"));

        String expected_three = "£120.59";
        List<String> items_three = new ArrayList<>(Arrays.asList("0002", "0002", "0003"));

        // When
        checkoutService.scan(items_one);
        String totalAmount_one = checkoutService.total();

        checkoutService.scan(items_two);
        String totalAmount_two = checkoutService.total();

        checkoutService.scan(items_three);
        String totalAmount_three = checkoutService.total();

        // Then
        assertEquals(expected_one, totalAmount_one);
        assertEquals(expected_two, totalAmount_two);
        assertEquals(expected_three, totalAmount_three);
    }

    @Test
    public void shouldScanTheItemsAndApplyBuyXOrMoreRule() {

        // Given
        String expected_one = "£114.97";
        List<String> items_one = new ArrayList<>(Arrays.asList("0001", "0001", "0002", "0003"));

        String expected_two = "£68.97";
        List<String> items_two = new ArrayList<>(Arrays.asList("0001", "0001", "0001"));

        String expected_three = "£133.99";
        List<String> items_three = new ArrayList<>(Arrays.asList("0002", "0002", "0003"));

        // Create the list of rules to be applied
        List<PromotionalRule> promotionalRules = new ArrayList<>();
        promotionalRules.add(new BuyXOrMorePromotionalRule(itemWaterBottle.getCode(), new BigDecimal("22.99"), 2));

        // When
        checkoutService.updatePromotionalRules(promotionalRules);

        checkoutService.scan(items_one);
        String totalAmount_one = checkoutService.total();

        checkoutService.scan(items_two);
        String totalAmount_two = checkoutService.total();

        checkoutService.scan(items_three);
        String totalAmount_three = checkoutService.total();

        // Then
        assertEquals(expected_one, totalAmount_one);
        assertEquals(expected_two, totalAmount_two);
        assertEquals(expected_three, totalAmount_three);
    }

    @Test
    public void shouldScanTheItemsAndApplySpentOverRule() {

        // Given
        String expected_one = "£107.00"; // 118.89
        List<String> items_one = new ArrayList<>(Arrays.asList("0001", "0001", "0002", "0003"));

        String expected_two = "£74.85";
        List<String> items_two = new ArrayList<>(Arrays.asList("0001", "0001", "0001"));

        String expected_three = "£120.59";
        List<String> items_three = new ArrayList<>(Arrays.asList("0002", "0002", "0003"));

        // Create the list of rules to be applied
        List<PromotionalRule> promotionalRules = new ArrayList<>();
        promotionalRules.add(new SpentOverGetDiscountPromotionalRule(new BigDecimal("75"), 10D));

        // When
        checkoutService.updatePromotionalRules(promotionalRules);

        checkoutService.scan(items_one);
        String totalAmount_one = checkoutService.total();

        checkoutService.scan(items_two);
        String totalAmount_two = checkoutService.total();

        checkoutService.scan(items_three);
        String totalAmount_three = checkoutService.total();

        // Then
        assertEquals(expected_one, totalAmount_one);
        assertEquals(expected_two, totalAmount_two);
        assertEquals(expected_three, totalAmount_three);
    }

    @Test
    public void shouldScanTheItemsAndReturnTheTotalWithNoPromotionRules() {

        // Given
        String expected_one = "£118.89";
        List<String> items_one = new ArrayList<>(Arrays.asList("0001", "0001", "0002", "0003"));

        String expected_two = "£74.85";
        List<String> items_two = new ArrayList<>(Arrays.asList("0001", "0001", "0001"));

        String expected_three = "£133.99";
        List<String> items_three = new ArrayList<>(Arrays.asList("0002", "0002", "0003"));

        // When
        checkoutService.updatePromotionalRules(null);

        checkoutService.scan(items_one);
        String totalAmount_one = checkoutService.total();

        checkoutService.scan(items_two);
        String totalAmount_two = checkoutService.total();

        checkoutService.scan(items_three);
        String totalAmount_three = checkoutService.total();

        // Then
        assertEquals(expected_one, totalAmount_one);
        assertEquals(expected_two, totalAmount_two);
        assertEquals(expected_three, totalAmount_three);
    }
}
