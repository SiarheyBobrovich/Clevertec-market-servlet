package ru.clevertec.market.data;

import java.math.BigDecimal;

public record BasketProduct(int id, String description, BigDecimal price, int quantity, boolean isDiscount) {

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private int id;
        private String description;
        private BigDecimal price;
        private int quantity;
        private boolean isDiscount;

        private Builder() {
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setPrice(BigDecimal price) {
            this.price = price;
            return this;
        }

        public Builder setQuantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Builder setDiscount(boolean discount) {
            isDiscount = discount;
            return this;
        }

        public BasketProduct build() {
            return new BasketProduct(
                    this.id,
                    this.description,
                    this.price,
                    this.quantity,
                    this.isDiscount
            );
        }
    }
}
