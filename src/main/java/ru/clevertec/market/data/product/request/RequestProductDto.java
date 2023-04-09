package ru.clevertec.market.data.product.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public record RequestProductDto(

        @JsonProperty(required = true)
        String description,

        @JsonProperty(required = true)
        BigDecimal price,

        @JsonProperty(required = true)
        Integer quantity,

        @JsonProperty(required = true)
        Boolean isDiscount
) {
}
