package com.example.checkoutsystem.promotionalrules;

import com.example.checkoutsystem.model.Item;
import com.example.checkoutsystem.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BuyXOrMorePromotionalRule implements PromotionalRule{

    private static final Logger LOGGER = LoggerFactory.getLogger(BuyXOrMorePromotionalRule.class);

    private final String itemOnDiscountCode;

    private final BigDecimal reducedPrice;

    private final int numberOfItems;

    /**
     * This rule checks if there are itemOnDiscount in the order and if their number is >= numberOfItems
     * then applies the new reducedPrice for these items.
     * @param itemOnDiscountCode the code of the Item on discount
     * @param reducedPrice the new reduced price of the item
     * @param numberOfItems the quantity of items that should be in the order, in order for the discount to apply.
     */
    public BuyXOrMorePromotionalRule(String itemOnDiscountCode, BigDecimal reducedPrice, int numberOfItems) {
        this.itemOnDiscountCode = itemOnDiscountCode;
        this.reducedPrice = reducedPrice;
        this.numberOfItems = numberOfItems;
    }

    @Override
    public void applyRule(Order order) {
        Map<Item, Long> itemsInOrder = order.getItems();
        Optional<Map.Entry<Item, Long>> item = itemsInOrder.entrySet()
                .stream()
                .filter(e -> e.getKey().getCode().equals(itemOnDiscountCode))
                .findFirst();

        if (item.isPresent()) {
            long count = item.get().getValue();
            if (count >= numberOfItems) {
                BigDecimal priceWithDiscount = itemsInOrder.entrySet().stream()
                        .filter(e -> !e.getKey().getCode().equals(itemOnDiscountCode))
                        .collect(Collectors.toList())
                        .stream()
                        .map(e -> e.getKey().getPrice().multiply(BigDecimal.valueOf(e.getValue())))
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .add(reducedPrice.multiply(BigDecimal.valueOf(count)));

                order.setTotalPrice(priceWithDiscount);

                LOGGER.info("{}X{} initial price: {}  Price with discount: {}",
                        itemOnDiscountCode, count, item.get().getKey().getPrice(), reducedPrice);
            }
        }
    }
}
