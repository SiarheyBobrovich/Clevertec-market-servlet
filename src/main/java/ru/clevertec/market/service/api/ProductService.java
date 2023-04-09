package ru.clevertec.market.service.api;

import ru.clevertec.market.data.product.request.RequestProductDto;
import ru.clevertec.market.data.product.response.ResponseProductDto;
import ru.clevertec.market.pagination.Page;
import ru.clevertec.market.pagination.Pageable;

import java.util.List;

public interface ProductService {

    /**
     * Find a product in repository by ID
     *
     * @param id product ID
     * @return a current product
     */
    ResponseProductDto getById(Integer id);

    /**
     * Find all products in repository
     *
     * @return all products from repository
     */
    List<ResponseProductDto> getAll();

    /**
     * Find page of products in repository
     *
     * @param page page of products (page number: min 0)
     * @return all products from repository (page number: min 1)
     */
    Page<ResponseProductDto> getAll(Pageable page);

    /**
     * Save a new product in repository
     *
     * @param dto product dto to save
     */
    Integer save(RequestProductDto dto);

    /**
     * Update all fields of product by ID
     *
     * @param id  a product ID
     * @param dto product dto to update
     */
    void update(Integer id, RequestProductDto dto);

    /**
     * Delete a product from repository by ID
     *
     * @param id a product ID
     */
    void delete(Integer id);
}
