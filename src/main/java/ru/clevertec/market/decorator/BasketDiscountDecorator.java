package ru.clevertec.market.decorator;

import lombok.RequiredArgsConstructor;
import ru.clevertec.market.data.Basket;
import ru.clevertec.market.entity.MarketProduct;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
public class BasketDiscountDecorator implements Basket {

    private final Basket basket;

    @Override
    public List<BasketProductQuantityDecorator> getProducts() {
        return basket.getProducts();
    }

    @Override
    public void addProduct(MarketProduct product, int quantity) {
        basket.addProduct(product, quantity);
    }

    @Override
    public BigDecimal getTotalPrice() {
        return basket.getTotalPrice();
    }

    public BigDecimal getDiscount(int discount) {
        BigDecimal currentDiscount = BigDecimal.valueOf(discount).multiply(new BigDecimal("0.01"));

        return basket.getProducts().stream()
                .filter(p -> p.isDiscount() && p.getQuantity() >= 5)
                .map(p -> p.getPrice().multiply(BigDecimal.valueOf(p.getQuantity())))
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO)
                .multiply(currentDiscount);
    }
}
