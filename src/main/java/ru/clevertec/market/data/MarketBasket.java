package ru.clevertec.market.data;


import ru.clevertec.market.decorator.BasketProductQuantityDecorator;
import ru.clevertec.market.entity.MarketProduct;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MarketBasket implements Basket {

    private final Map<Integer, BasketProductQuantityDecorator> products;

    public MarketBasket() {
        products = new HashMap<>();
    }

    @Override
    public List<BasketProductQuantityDecorator> getProducts() {
        return products.values().stream().toList();
    }

    @Override
    public void addProduct(MarketProduct product, int quantity) {
        int id = product.getId();
        quantity += !products.containsKey(id) ? 0 : products.get(id).getQuantity();

        products.put(
                id,
                new BasketProductQuantityDecorator(
                        BasketProduct.builder()
                                .setId(id)
                                .setDescription(product.getDescription())
                                .setPrice(product.getPrice())
                                .setDiscount(product.isDiscount())
                                .setQuantity(quantity)
                                .build()));
    }

    @Override
    public BigDecimal getTotalPrice() {
        return products.values().stream()
                .map(BasketProductQuantityDecorator::getTotalPrice)
                .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MarketBasket that)) return false;
        return Objects.equals(products, that.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(products);
    }

    @Override
    public String toString() {
        return "MarketBasket{" +
                "products=" + products +
                '}';
    }
}
