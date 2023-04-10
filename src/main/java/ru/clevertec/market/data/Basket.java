package ru.clevertec.market.data;


import ru.clevertec.market.decorator.BasketProductQuantityDecorator;
import ru.clevertec.market.entity.MarketProduct;

import java.math.BigDecimal;
import java.util.List;

public interface Basket {

    /**
     * Returns List of Products-quantities
     * @return - List of products
     */
    List<BasketProductQuantityDecorator> getProducts();

    /**
     * Add a new product to current basket
     * @param product - Product from db
     * @param quantity - A quantity of products
     */
    void addProduct(MarketProduct product, int quantity);

    /**
     * Returns a total of products
     * @return total of products as BigDecimal
     */
    BigDecimal getTotalPrice();
}
