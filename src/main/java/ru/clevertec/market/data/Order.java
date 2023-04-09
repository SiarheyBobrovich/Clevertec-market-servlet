package ru.clevertec.market.data;

import java.util.List;

public interface Order {

    /**
     * Returns a List of order entries
     * @return - List of entries
     */
    List<OrderEntry> orderEntries();

    /**
     * Returns card number
     * @return - A card number if exists, else null
     */
    Integer discountCardNumber();
}
