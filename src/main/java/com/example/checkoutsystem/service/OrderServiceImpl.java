package com.example.checkoutsystem.service;

import com.example.checkoutsystem.model.Item;
import com.example.checkoutsystem.model.Order;
import com.example.checkoutsystem.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

    private final ItemRepository itemRepository;

    @Autowired
    public OrderServiceImpl(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    @Override
    public Map<Item, Long> fetchItemsGroupedByQuantity(List<String> scannedItems) {

        Map<String, Long> groupedItemsByQnt = scannedItems.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        return groupedItemsByQnt.keySet().stream().map(e -> {
            Item itemInStock = itemRepository.findByCode(e);
            if (itemInStock == null) {
                throw new IllegalArgumentException("Invalid item code.");
            }
            return itemInStock;
        }).collect(Collectors.toMap(Function.identity(), key -> groupedItemsByQnt.get(key.getCode())));
    }

    @Override
    public void updateTotalPrice(Order order) {
        BigDecimal finalPrice = order.getItems().entrySet().stream()
                .map(e -> e.getKey().getPrice().multiply(BigDecimal.valueOf(e.getValue())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        order.setTotalPrice(finalPrice);
    }
}
