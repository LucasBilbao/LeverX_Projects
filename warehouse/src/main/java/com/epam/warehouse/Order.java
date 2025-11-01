package com.epam.warehouse;

import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@NoArgsConstructor
public class Order {
    private final Map<Product, Integer> items = new ConcurrentHashMap<>();

    public void addItem(Product product, int quantity) {
        this.items.put(product, quantity);
    }

    public Map<Product, Integer> getItems() {
        return Collections.unmodifiableMap(this.items);
    }

    @Override
    public String toString() {
        return "Orders" + this.items;
    }
}
