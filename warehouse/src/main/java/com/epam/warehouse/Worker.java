package com.epam.warehouse;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

@RequiredArgsConstructor
class Worker implements Runnable {
    private final Warehouse warehouse;
    private final BlockingQueue<Order> queue;
    private final List<Order> processedOrders;
    private final AtomicLong totalProfit;

    @Override
    public void run() {
        try {
            while (true) {
                Order order = this.queue.poll(1, TimeUnit.SECONDS);
                if (order == null) {
                    break;
                }

                if (!this.warehouse.fulfillOrder(order)) {
                    System.out.println("Failed: " + order);
                }

                long profit = (long) order.getItems()
                        .entrySet()
                        .stream()
                        .mapToDouble(e -> e.getKey().price() * e.getValue())
                        .sum();
                this.totalProfit.addAndGet(profit);
                this.processedOrders.add(order);
                System.out.println("Processed: " + order);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
