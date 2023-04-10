package ru.clevertec.market.exception;

public class DiscountException extends IllegalArgumentException {

    public DiscountException() {
        super("Discount must be between 0 and 100");
    }
}
