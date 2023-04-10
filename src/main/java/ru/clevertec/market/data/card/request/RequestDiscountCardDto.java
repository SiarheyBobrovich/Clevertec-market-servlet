package ru.clevertec.market.data.card.request;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RequestDiscountCardDto(

        @JsonProperty(required = true)
        Byte discount
) {
}
