package ru.clevertec.market.data.product.response;

import java.math.BigDecimal;

public record ResponseProductDto(
        Integer id,
        String description,
        BigDecimal price,
        Integer quantity,
        Boolean isDiscount
) {
}
