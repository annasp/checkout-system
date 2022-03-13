package com.example.checkoutsystem;

import com.example.checkoutsystem.promotionalrules.BuyXOrMorePromotionalRule;
import com.example.checkoutsystem.promotionalrules.PromotionalRule;
import com.example.checkoutsystem.promotionalrules.SpentOverGetDiscountPromotionalRule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class CheckoutSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(CheckoutSystemApplication.class, args);
    }

    @Bean
    public List<PromotionalRule> promotionalRules() {
        // The SpentOverGetDiscountPromotionalRule should be applied last, so the rules should be inserted in order in the list.
        // Instead of using insertion order, another parameter could have been used to define in which order the rules should apply.
        List<PromotionalRule> promotionalRules = new ArrayList<>();
        promotionalRules.add(new BuyXOrMorePromotionalRule("0001", new BigDecimal("22.99"), 2));
        promotionalRules.add(new SpentOverGetDiscountPromotionalRule(new BigDecimal("75"), 10D));

        return promotionalRules;
    }

}
