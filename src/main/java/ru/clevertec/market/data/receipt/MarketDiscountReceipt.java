package ru.clevertec.market.data.receipt;

import lombok.Getter;
import ru.clevertec.market.decorator.BasketDiscountDecorator;
import ru.clevertec.market.entity.MarketDiscountCard;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class MarketDiscountReceipt extends AbstractReceipt {

    protected static final String TOTAL_FORMAT = """
            TAXABLE TOT_                        $%s
            VAT17%%                              $%s
            DISCOUNT                            $%s
            TOTAL                               $%s
            """;

    private final String total;

    public MarketDiscountReceipt(LocalDateTime dateTime,
                                 BasketDiscountDecorator basket,
                                 int cashier,
                                 MarketDiscountCard card) {
        super(dateTime, basket, cashier);
        total = createTotal(basket, card);
    }

    private String createTotal(BasketDiscountDecorator basket, MarketDiscountCard card) {
        BigDecimal totalPrice = basket.getTotalPrice();
        BigDecimal discount = basket.getDiscount(card.getDiscount());
        BigDecimal totalPriceWithDiscount = totalPrice.subtract(discount);

        return String.format(
                TOTAL_FORMAT,
                setScaleTo2(getTaxableTot(totalPrice)),
                setScaleTo2(getVat(totalPrice)),
                setScaleTo2(discount),
                setScaleTo2(totalPriceWithDiscount)
        );
    }
}
