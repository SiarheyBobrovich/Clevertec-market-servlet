package ru.clevertec.market.data.receipt;

import lombok.Getter;
import ru.clevertec.market.data.Basket;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
public class MarketReceipt extends AbstractReceipt {
    protected static final String TOTAL_BLOCK_FORMAT =
            """
                    TAXABLE TOT_                        $%s
                    VAT17%%                              $%s
                    TOTAL                               $%s
                    """;

    private final String total;

    public MarketReceipt(LocalDateTime dateTime, Basket basket, int cashier) {
        super(dateTime, basket, cashier);
        this.total = createTotal(basket);
    }

    private String createTotal(Basket basket) {
        final BigDecimal totalPrice = basket.getTotalPrice();
        return String.format(TOTAL_BLOCK_FORMAT,
                setScaleTo2(getTaxableTot(totalPrice)),
                setScaleTo2(getVat(totalPrice)),
                setScaleTo2(totalPrice)
        );
    }
}