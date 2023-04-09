package ru.clevertec.market.data.receipt;

import ru.clevertec.market.data.Basket;
import ru.clevertec.market.decorator.BasketProductQuantityDecorator;

import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public abstract class AbstractReceipt implements Receipt {

    protected static final String BLOCK_SEPARATOR = "--------------------------------------------";
    protected static final String BASE_TITLE =
            """
                                CASH RECEIPT
                            SUPERMARKET     123
                         12, MILKYWAY GALAXY/ Earth
                            Tel : 123-456-7890
                    CASHIER: №%d
                                              DATE:  %s
                                              TIME:  %s
                    """;

    protected static final String BASE_BODY =
            """
                    QTY  DESCRIPTION         PRICE      TOTAL
                    """;

    private final String title;
    private final String body;

    protected AbstractReceipt(LocalDateTime dateTime, Basket basket, int cashier) {
        title = String.format(
                BASE_TITLE,
                cashier,
                dateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                dateTime.format(DateTimeFormatter.ofPattern("hh:mm:ss")));
        body = createBody(basket);
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    private String createBody(Basket basket) {
        List<BasketProductQuantityDecorator> products = basket.getProducts();
        StringBuilder info = new StringBuilder(BASE_BODY);

        products.forEach(product -> {
            int quantity = product.getQuantity();
            String productDescription = product.getDescription();
            BigDecimal productPrice = product.getPrice();
            BigDecimal totalPrice = product.getTotalPrice();

            info.append(normalizeString(5, String.valueOf(quantity)))
                    .append(normalizeString(20, productDescription))
                    .append("$")
                    .append(normalizeString(10, setScaleTo2(productPrice).toString()))
                    .append("$")
                    .append(normalizeString(10, setScaleTo2(totalPrice).toString()))
                    .append('\n');
        });

        return info.toString();
    }

    @Override
    public void print(PrintStream out) {
        out.print(getTitle());
        out.println(BLOCK_SEPARATOR);
        out.print(getBody());
        out.println(BLOCK_SEPARATOR);
        out.println(getTotal());
    }

    protected String normalizeString(int length, String description) {
        int currentLength = description.length();
        String result;
        if (currentLength == length) {
            result = description;
        } else if (currentLength > length) {
            result = description.substring(length);
        } else {
            result = description + " ".repeat(length - currentLength);
        }

        return result;
    }

    protected BigDecimal getVat(BigDecimal currentTotal) {
        return currentTotal.multiply(new BigDecimal("0.17"));
    }

    protected BigDecimal getTaxableTot(BigDecimal currentTotal) {
        return currentTotal.multiply(new BigDecimal("0.83"));
    }

    protected BigDecimal setScaleTo2(BigDecimal decimal) {
        return decimal.setScale(2, RoundingMode.HALF_UP);
    }
}
