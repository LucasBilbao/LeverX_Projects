package com.epam.warehouse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

public class WarehouseApplication {

    public static void main(String[] args) throws InterruptedException {
        List<Product> catalog = List.of(
                new Product("Laptop", 1200),
                new Product("Phone", 800),
                new Product("Tablet", 600),
                new Product("Headphones", 150),
                new Product("Mouse", 50)
        );

        Warehouse warehouse = new Warehouse(catalog);
        BlockingQueue<Order> queue = new LinkedBlockingQueue<>();
        List<Order> processedOrders = Collections.synchronizedList(new ArrayList<>());
        AtomicLong totalProfit = new AtomicLong(0);

        ExecutorService customers = Executors.newFixedThreadPool(3);
        ExecutorService workers = Executors.newFixedThreadPool(2);
        customers.execute(new Customer("Alice", catalog, queue));
        customers.execute(new Customer("Bob", catalog, queue));
        customers.execute(new Customer("Charlie", catalog, queue));

        customers.shutdown();
        customers.awaitTermination(5, TimeUnit.SECONDS);

        workers.execute(new Worker(warehouse, queue, processedOrders, totalProfit));
        workers.execute(new Worker(warehouse, queue, processedOrders, totalProfit));

        workers.shutdown();
        workers.awaitTermination(5, TimeUnit.SECONDS);

        System.out.println("\n--- RESULTS ---");

        int totalOrders = processedOrders.size();
        System.out.println("Total orders: " + totalOrders);

        System.out.println("Total profit: $" + totalProfit.get());

        Map<Product, Integer> productSales = processedOrders.parallelStream()
                .flatMap(
                        o -> o.getItems()
                                .entrySet()
                                .stream()
                )
                .collect(Collectors.toConcurrentMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        Integer::sum
                ));

        productSales.entrySet()
                .stream()
                .sorted(Map.Entry.<Product, Integer>comparingByValue().reversed())
                .limit(3)
                .forEach(e -> System.out.println(e.getKey().name() + ": " + e.getValue() + " sold"));
    }
}
