package ru.clevertec.market.dao.api;

import ru.clevertec.market.entity.MarketDiscountCard;
import ru.clevertec.market.pagination.Page;
import ru.clevertec.market.pagination.Pageable;

import java.util.Optional;

public interface DiscountCardDao {

    /**
     * Returns saved Discount card
     * @param id - Discount card id
     * @return - Discount card if contains
     */
    Optional<MarketDiscountCard> getById(Integer id);

    /**
     * Find page of discount cards
     *
     * @param page a page number and a page size
     * @return page of cards
     */
    Page<MarketDiscountCard> findAll(Pageable page);

    /**
     * Save a new discount card
     * @param discountCard current card(with ID) or new card(without ID)
     * @return a card ID
     */
    Integer save(MarketDiscountCard discountCard);

    /**
     * Delete existed card
     * @param id a card ID
     */
    void delete(Integer id);
}
