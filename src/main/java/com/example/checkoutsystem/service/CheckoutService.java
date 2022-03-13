package com.example.checkoutsystem.service;

import com.example.checkoutsystem.promotionalrules.PromotionalRule;
import java.util.List;

public interface CheckoutService {

    /**
     * Scan the list of items
     * @param productCodeList
     */
    void scan(List<String> productCodeList);

    /**
     * Get the total amount
     * @return
     */
    String total();

    /**
     * Method to reset the promotional rules
     * @param promotionalRules
     */
    void updatePromotionalRules(List<PromotionalRule> promotionalRules);
}
