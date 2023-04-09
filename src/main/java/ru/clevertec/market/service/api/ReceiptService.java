package ru.clevertec.market.service.api;

import ru.clevertec.market.data.Order;
import ru.clevertec.market.data.receipt.Receipt;

public interface ReceiptService {

    /**
     * Calculate and return bill for order
     *
     * @param order User order
     * @return Bill
     */
    Receipt getReceipt(Order order);
}
