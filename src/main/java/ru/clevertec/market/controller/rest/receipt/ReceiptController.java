package ru.clevertec.market.controller.rest.receipt;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.market.config.ApplicationConfig;
import ru.clevertec.market.data.MarketOrder;
import ru.clevertec.market.data.Order;
import ru.clevertec.market.factory.ReceiptFactory;
import ru.clevertec.market.service.PdfServiceImpl;
import ru.clevertec.market.service.api.PdfService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebServlet("/receipts/pdf")
public class ReceiptController extends HttpServlet {

    private final PdfService service;

    public ReceiptController() {
        service = new PdfServiceImpl(
                ApplicationConfig.getProductService(),
                ApplicationConfig.getDiscountCardService(),
                new ReceiptFactory());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final Integer card;
        final List<Integer> itemIdList;
        final Path path;
        try {
            itemIdList = req.getParameterMap().entrySet().stream()
                    .filter(e -> "itemId".equals(e.getKey()))
                    .flatMap(e -> Arrays.stream(e.getValue()))
                    .map(Integer::parseInt)
                    .toList();
            card = Optional.ofNullable(req.getParameter("card"))
                    .map(Integer::parseInt)
                    .orElse(null);
        } catch (NumberFormatException e) {
            resp.sendError(400);
            return;
        }
        final Order order = MarketOrder.builder()
                .addItemsId(itemIdList)
                .addDiscountCard(card)
                .build();
        path = service.getPath(order);

        resp.setStatus(200);
        resp.setContentType("application/pdf");
        resp.setContentLengthLong(path.toFile().length());
        Files.copy(path, resp.getOutputStream());
    }
}
