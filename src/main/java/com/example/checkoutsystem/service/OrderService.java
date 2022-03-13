package com.example.checkoutsystem.service;

import com.example.checkoutsystem.model.Item;
import com.example.checkoutsystem.model.Order;

import java.util.List;
import java.util.Map;

public interface OrderService {

    /**
     * Fetch the items from the database and group the same items. If the items does not exit the method throws an exception.
     * @param scannedItems a list with the codes of the items.
     * @return a Map with the Item as key and its quantity as value.
     */
    Map<Item, Long> fetchItemsGroupedByQuantity(List<String> scannedItems);

    /**
     * Method to sum the price of all the items included in the order and update the total price of the order.
     * @param order the order including all the scanned items
     */
    void updateTotalPrice(Order order);
}
