package com.epam.warehouse;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Warehouse {
    private final ConcurrentHashMap<Product, Integer> stock = new ConcurrentHashMap<>();

    public Warehouse(List<Product> products) {
        products.forEach(p -> this.stock.put(p, 10));
    }

    public boolean fulfillOrder(Order order) {
        synchronized (this) {
            for (var e : order.getItems().entrySet()) {
                int available = this.stock.getOrDefault(e.getKey(), 0);
                if (available < e.getValue()) {
                    return false;
                }
            }
            for (var e : order.getItems().entrySet()) {
                this.stock.put(e.getKey(), this.stock.get(e.getKey()) - e.getValue());
            }
            return true;
        }
    }

    public Map<Product, Integer> getStock() {
        return Collections.unmodifiableMap(this.stock);
    }
}
