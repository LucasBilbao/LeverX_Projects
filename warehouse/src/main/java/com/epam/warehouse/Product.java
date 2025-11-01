package com.epam.warehouse;

public record Product(String name, float price) {

    @Override
    public String toString() {
        return this.name;
    }
}
