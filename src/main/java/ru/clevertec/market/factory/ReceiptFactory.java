package ru.clevertec.market.factory;

import ru.clevertec.market.data.Basket;
import ru.clevertec.market.data.receipt.MarketDiscountReceipt;
import ru.clevertec.market.data.receipt.MarketReceipt;
import ru.clevertec.market.data.receipt.Receipt;
import ru.clevertec.market.decorator.BasketDiscountDecorator;
import ru.clevertec.market.entity.MarketDiscountCard;

import java.time.LocalDateTime;

public class ReceiptFactory {

    public Receipt create(Basket basket, MarketDiscountCard card, int cashier) {
        Receipt receipt;
        if (card == null) {
            receipt = createStandardReceipt(basket, cashier);
        } else {
            receipt = createDiscountReceipt(basket, cashier, card);
        }
        return receipt;
    }

    private Receipt createDiscountReceipt(Basket basket, int cashier, MarketDiscountCard card) {
        return new MarketDiscountReceipt(
                LocalDateTime.now(),
                new BasketDiscountDecorator(basket),
                cashier,
                card
        );
    }

    private Receipt createStandardReceipt(Basket basket, int cashier) {
        return new MarketReceipt(
                LocalDateTime.now(),
                basket,
                cashier
        );
    }
}
