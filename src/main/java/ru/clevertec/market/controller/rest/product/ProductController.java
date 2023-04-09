package ru.clevertec.market.controller.rest.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import ru.clevertec.market.config.ApplicationConfig;
import ru.clevertec.market.data.product.request.RequestProductDto;
import ru.clevertec.market.data.product.response.ResponseProductDto;
import ru.clevertec.market.exception.BadRequestException;
import ru.clevertec.market.pagination.Page;
import ru.clevertec.market.pagination.Pageable;
import ru.clevertec.market.service.api.ProductService;
import ru.clevertec.market.util.ControllerUtil;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@WebServlet("/products/*")
@RequiredArgsConstructor
public class ProductController extends HttpServlet {

    private final ProductService service;
    private final ObjectMapper mapper;

    public ProductController() {
        this.mapper = ApplicationConfig.getObjectMapper();
        this.service = ApplicationConfig.getProductService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();

        if (Objects.isNull(pathInfo)) {
            final Pageable pageable = Pageable.of(req.getParameter("page"), req.getParameter("size"));
            sendPage(service.getAll(pageable), resp);
        } else if (isAll(pathInfo)) {
            sendList(service.getAll(), resp);
        } else if (ControllerUtil.isId(pathInfo)) {
            String id = pathInfo.substring(1);
            ResponseProductDto byId;
            byId = service.getById(Integer.parseInt(id));
            String value = mapper.writeValueAsString(byId);
            sendJsonResponse(value, resp);
        } else {
            resp.sendError(404, String.format("The requested resource [%s] is not available", req.getRequestURI()));
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id;
        try {
            RequestProductDto requestProductDto = mapper.readValue(req.getInputStream(), RequestProductDto.class);
            id = service.save(requestProductDto);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + req.getServletPath() + "/" + id);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        String id;
        if (!ControllerUtil.isId(pathInfo)) {
            throw new BadRequestException("Id must be set");
        }
        try {
            id = pathInfo.substring(1);
            RequestProductDto requestProductDto = mapper.readValue(req.getInputStream(), RequestProductDto.class);
            service.update(Integer.parseInt(id), requestProductDto);
        } catch (IOException e) {
            throw new BadRequestException(e.getMessage());
        }
        resp.sendRedirect(req.getContextPath() + req.getServletPath() + "/" + id);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        String id;
        if (!ControllerUtil.isId(pathInfo)) {
            throw new BadRequestException("Id must be set");
        }
        id = pathInfo.substring(1);
        service.delete(Integer.parseInt(id));
        resp.setStatus(204);
    }

    private void sendList(List<ResponseProductDto> dtoPage, HttpServletResponse resp) throws IOException {
        sendJsonResponse(mapper.writeValueAsString(dtoPage), resp);
    }

    private void sendPage(Page<ResponseProductDto> all, HttpServletResponse resp) throws IOException {
        sendJsonResponse(mapper.writeValueAsString(all), resp);

    }

    private void sendJsonResponse(String value, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.setStatus(200);
        response.getWriter().println(value);
    }

    private boolean isAll(String pathInfo) {
        return Objects.equals("/all", pathInfo);
    }
}
