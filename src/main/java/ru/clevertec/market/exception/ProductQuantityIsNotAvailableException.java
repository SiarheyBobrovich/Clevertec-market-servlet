package ru.clevertec.market.exception;

public class ProductQuantityIsNotAvailableException extends IllegalStateException {
    public ProductQuantityIsNotAvailableException(int id, int quantity) {
        super("Product " + id + ", quantity: " + quantity + " is not available");
    }
}
