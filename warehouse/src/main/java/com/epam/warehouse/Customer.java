package com.epam.warehouse;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;

@RequiredArgsConstructor
class Customer implements Runnable {
    private final String name;
    private final List<Product> catalog;
    private final BlockingQueue<Order> queue;
    private final Random random = new Random();

    @Override
    public void run() {
        try {
            for (int i = 0; i < 3; i++) {
                Order order = new Order();
                for (int j = 0; j < this.random.nextInt(3) + 1; j++) {
                    Product p = this.catalog.get(this.random.nextInt(this.catalog.size()));
                    order.addItem(p, this.random.nextInt(3) + 1);
                }
                System.out.println(this.name + " placed: " + order);
                this.queue.put(order);
                Thread.sleep(200);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
