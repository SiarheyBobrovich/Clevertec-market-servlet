package ru.clevertec.market.dao.api;

import ru.clevertec.market.entity.MarketProduct;
import ru.clevertec.market.exception.ProductNotFoundException;
import ru.clevertec.market.pagination.Page;
import ru.clevertec.market.pagination.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductDao {

    /**
     * Returns saved product
     *
     * @param id - product id
     * @return - Product if contains
     * @throws ProductNotFoundException - if not found
     */
    Optional<MarketProduct> findById(Integer id);

    /**
     * Check Product in repository
     *
     * @param id - Product id
     * @return - true if exists
     */
    boolean exists(Integer id);

    /**
     * Check Product in repository
     *
     * @param id       - Product id
     * @param quantity - Product quantity
     * @return - true if exists, and quantity is available
     */
    boolean isExistsAndQuantityAvailable(Integer id, Integer quantity);

    /**
     * Update current product
     *
     * @param product - updated
     */
    void update(MarketProduct product);

    /**
     * Delete a product by ID
     *
     * @param id the product ID
     */
    void delete(Integer id);

    /**
     * Save a new product
     *
     * @param product new product without id
     * @return the product ID
     */
    Integer save(MarketProduct product);

    /**
     * Find page of products
     *
     * @param page a page number & a page size
     * @return page of products
     */
    Page<MarketProduct> findAll(Pageable page);

    /**
     * Find all products
     *
     * @return List of all products
     */
    List<MarketProduct> findAll();
}
