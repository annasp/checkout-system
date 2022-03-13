package com.example.checkoutsystem.promotionalrules;

import com.example.checkoutsystem.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class SpentOverGetDiscountPromotionalRule implements PromotionalRule {

    private static final Logger LOGGER = LoggerFactory.getLogger(SpentOverGetDiscountPromotionalRule.class);

    private final BigDecimal priceOver;

    private final Double discountPercentage;

    /**
     * This rule applies on the total price of the order. If the total amount of the order (sum of all items' price)
     * is over the given priceOver then the given discount - discountPercentage - is applied to final total amount
     * This rule should be applied last.
     * @param priceOver the price over which the discount applies
     * @param discountPercentage the discount percentage value to apply
     */
    public SpentOverGetDiscountPromotionalRule(BigDecimal priceOver, Double discountPercentage) {
        this.priceOver = priceOver;
        this.discountPercentage = discountPercentage;
    }

    public void applyRule(Order order) {
        BigDecimal totalPrice = order.getTotalPrice();

        if (totalPrice.compareTo(priceOver) > 0) {
            BigDecimal priceWitDiscount = totalPrice.subtract(totalPrice
                    .multiply(BigDecimal.valueOf(discountPercentage))
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.CEILING));
            order.setTotalPrice(priceWitDiscount);

            LOGGER.info("{}X{}% = {}", totalPrice, discountPercentage, priceWitDiscount);
        }
    }
}
