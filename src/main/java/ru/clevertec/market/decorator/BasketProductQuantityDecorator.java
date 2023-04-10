package ru.clevertec.market.decorator;

import ru.clevertec.market.data.BasketProduct;

import java.math.BigDecimal;

public class BasketProductQuantityDecorator {

    private final BasketProduct basketProduct;

    public BasketProductQuantityDecorator(BasketProduct basketProduct) {
        this.basketProduct = basketProduct;
    }

    public int getId() {
        return basketProduct.id();
    }

    public BigDecimal getPrice() {
        return basketProduct.price();
    }

    public String getDescription() {
        return basketProduct.description();
    }

    public boolean isDiscount() {
        return basketProduct.isDiscount();
    }

    public int getQuantity() {
        return basketProduct.quantity();
    }

    public BigDecimal getTotalPrice() {
        return basketProduct.price().multiply(BigDecimal.valueOf(basketProduct.quantity()));
    }
}
