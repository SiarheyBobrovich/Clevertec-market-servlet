package ru.clevertec.market.exception;

public class DiscountCardNotFoundException extends IllegalArgumentException {
    public DiscountCardNotFoundException(int discountCardNumber) {
        super(String.format("Card with id '%d' not found", discountCardNumber));
    }
}
