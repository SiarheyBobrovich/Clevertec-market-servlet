package ru.clevertec.market.data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public record MarketOrder(List<OrderEntry> orderEntries, Integer discountCardNumber) implements Order {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final List<OrderEntry> orderEntries;
        private Integer discountCardNumber;

        Builder() {
            orderEntries = new ArrayList<>();
        }

        public Builder addOrderEntry(OrderEntry entry) {
            this.orderEntries.add(entry);
            return this;
        }

        public Builder addItemsId(List<Integer> itemsId) {
            itemsId.stream()
                    .collect(Collectors.groupingBy(x -> x, Collectors.counting()))
                    .entrySet().stream()
                    .map(id -> new MarketOrderEntry(id.getKey(), id.getValue().intValue()))
                    .forEach(this.orderEntries::add);
            return this;
        }

        public Builder addDiscountCard(Integer discountCardNumber) {
            this.discountCardNumber = discountCardNumber;
            return this;
        }

        public Order build() {
            return new MarketOrder(orderEntries, discountCardNumber);
        }
    }
}
