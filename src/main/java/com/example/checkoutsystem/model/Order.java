package com.example.checkoutsystem.model;

import java.math.BigDecimal;
import java.util.Map;

public class Order {

    private Long id;

    private Map<Item, Long> items;

    private BigDecimal totalPrice;

    public Order(Map<Item, Long> items) {
        this.items = items;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<Item, Long> getItems() {
        return items;
    }

    public void setItems(Map<Item, Long> items) {
        this.items = items;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
