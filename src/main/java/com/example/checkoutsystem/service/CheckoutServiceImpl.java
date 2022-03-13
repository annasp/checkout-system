package com.example.checkoutsystem.service;

import com.example.checkoutsystem.model.Item;
import com.example.checkoutsystem.model.Order;
import com.example.checkoutsystem.promotionalrules.PromotionalRule;
import com.example.checkoutsystem.service.exceptions.EmptyCheckoutListException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class CheckoutServiceImpl implements CheckoutService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CheckoutServiceImpl.class);

    @Value("${checkout.currency}")
    private String currency;

    private List<PromotionalRule> promotionalRules;
    private final OrderService orderService;
    private Order order;

    public CheckoutServiceImpl(List<PromotionalRule> promotionalRules, OrderService orderService) {
        this.orderService = orderService;
        this.promotionalRules = promotionalRules;
    }

    @Override
    public void scan(List<String> productCodeList) {

        if (productCodeList == null || productCodeList.isEmpty()) {
            throw new EmptyCheckoutListException("The checkout list is empty.");
        }

        Map<Item, Long> itemsGroupedByQuantity = orderService.fetchItemsGroupedByQuantity(productCodeList);

        this.order = new Order(itemsGroupedByQuantity);
        orderService.updateTotalPrice(order);

        LOGGER.info("Initial total price: {}", order.getTotalPrice());
    }

    @Override
    public String total() {
        if (promotionalRules != null) {
            promotionalRules.forEach(rule -> rule.applyRule(order));
        }

        return currency + order.getTotalPrice();
    }

    @Override
    public void updatePromotionalRules(List<PromotionalRule> promotionalRules) {
        this.promotionalRules = promotionalRules;
    }

}
