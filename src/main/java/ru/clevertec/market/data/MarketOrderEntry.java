package ru.clevertec.market.data;

public record MarketOrderEntry(Integer id, Integer quantity) implements OrderEntry {
}
