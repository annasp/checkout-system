package com.example.checkoutsystem.promotionalrules;

import com.example.checkoutsystem.model.Order;

@FunctionalInterface
public interface PromotionalRule {

    void applyRule(Order order);

}
