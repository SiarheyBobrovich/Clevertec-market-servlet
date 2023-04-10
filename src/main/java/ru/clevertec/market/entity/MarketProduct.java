package ru.clevertec.market.entity;

import lombok.*;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class MarketProduct {
    private int id;
    private String description;
    private BigDecimal price;
    private int quantity;
    private boolean isDiscount;
}