package ru.clevertec.market.service.api;

import ru.clevertec.market.data.card.request.RequestDiscountCardDto;
import ru.clevertec.market.data.card.response.ResponseDiscountCardDto;
import ru.clevertec.market.pagination.Page;
import ru.clevertec.market.pagination.Pageable;

public interface DiscountCardService {

    /**
     * Find page of discount cards
     * @param pageable current page number with size (page number: min 0)
     * @return page of cards (page number: min 1)
     */
    Page<ResponseDiscountCardDto> findAll(Pageable pageable);

    /**
     * Find a discount card in repository by ID
     *
     * @param id the card ID
     * @return a current card
     */
    ResponseDiscountCardDto getById(Integer id);

    /**
     * Update all fields of discount card by ID
     *
     * @param id  a card ID
     * @param dto card dto to update
     */
    void update(Integer id, RequestDiscountCardDto dto);

    /**
     * Delete a discount card from repository by ID
     *
     * @param id a card ID
     */
    void delete(Integer id);

    /**
     * Save a new discount card in repository
     *
     * @param dto a card dto to save
     * @return card's id
     */
    Integer save(RequestDiscountCardDto dto);
}
