package ru.clevertec.market.controller.rest.card;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.clevertec.market.config.ApplicationConfig;
import ru.clevertec.market.data.card.request.RequestDiscountCardDto;
import ru.clevertec.market.data.card.response.ResponseDiscountCardDto;
import ru.clevertec.market.exception.BadRequestException;
import ru.clevertec.market.pagination.Page;
import ru.clevertec.market.pagination.Pageable;
import ru.clevertec.market.service.api.DiscountCardService;
import ru.clevertec.market.util.ControllerUtil;

import java.io.IOException;
import java.util.Objects;

@WebServlet("/cards/*")
public class CardController extends HttpServlet {

    private final DiscountCardService cardService;
    private final ObjectMapper mapper;

    public CardController() {
        this.cardService = ApplicationConfig.getCardService();
        this.mapper = ApplicationConfig.getObjectMapper();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        if (Objects.isNull(pathInfo)) {
            final Pageable pageable = Pageable.of(req.getParameter("page"), req.getParameter("size"));
            Page<ResponseDiscountCardDto> page = cardService.findAll(pageable);
            sendJsonResponse(mapper.writeValueAsString(page), resp);
        } else if (ControllerUtil.isId(pathInfo)) {
            String id = pathInfo.substring(1);
            ResponseDiscountCardDto byId;
            byId = cardService.getById(Integer.parseInt(id));
            String value = mapper.writeValueAsString(byId);
            sendJsonResponse(value, resp);
        } else {
            resp.sendError(404, String.format("The requested resource [%s] is not available", req.getRequestURI()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id;
        try {
            RequestDiscountCardDto requestProductDto = mapper.readValue(req.getInputStream(), RequestDiscountCardDto.class);
            id = cardService.save(requestProductDto);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + req.getServletPath() + "/" + id);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String id;
        if (!ControllerUtil.isId(pathInfo)) {
            throw new BadRequestException("Id must be set");
        }
        try {
            id = pathInfo.substring(1);
            RequestDiscountCardDto requestProductDto = mapper.readValue(req.getInputStream(), RequestDiscountCardDto.class);
            cardService.update(Integer.parseInt(id), requestProductDto);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + req.getServletPath() + "/" + id);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();
        String id;
        if (!ControllerUtil.isId(pathInfo)) {
            throw new BadRequestException("Id must be set");
        }
        id = pathInfo.substring(1);
        cardService.delete(Integer.parseInt(id));
        resp.setStatus(204);
    }

    private void sendJsonResponse(String value, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setStatus(200);
        response.getWriter().println(value);
    }
}
