package ru.clevertec.market.service.api;

import ru.clevertec.market.data.Order;

import java.nio.file.Path;

public interface PdfService {

    /**
     * Creates a temporary Pdf file with a receipt, returns the path to the created file
     *
     * @param order a receipt order
     * @return path to temp file
     */
    Path getPath(Order order);
}
