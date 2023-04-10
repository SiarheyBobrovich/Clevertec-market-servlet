package ru.clevertec.market.exception;

public class ProductSqlException extends IllegalStateException {

    public ProductSqlException() {
        super("Data base is not available");
    }

    public ProductSqlException(String message) {
        super(message);
    }
}
