package ru.clevertec.market.exception;

public class ProductNotFoundException extends IllegalArgumentException {
    public ProductNotFoundException(int id) {
        super(String.format("Product with id '%d' not found", id));
    }
}
