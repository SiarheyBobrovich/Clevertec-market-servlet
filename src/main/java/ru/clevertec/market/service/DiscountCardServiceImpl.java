package ru.clevertec.market.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import ru.clevertec.market.dao.api.DiscountCardDao;
import ru.clevertec.market.data.card.request.RequestDiscountCardDto;
import ru.clevertec.market.data.card.response.ResponseDiscountCardDto;
import ru.clevertec.market.entity.MarketDiscountCard;
import ru.clevertec.market.exception.DiscountCardNotFoundException;
import ru.clevertec.market.exception.DiscountException;
import ru.clevertec.market.mapper.DiscountCardMapper;
import ru.clevertec.market.pagination.Page;
import ru.clevertec.market.pagination.PageImpl;
import ru.clevertec.market.pagination.Pageable;
import ru.clevertec.market.service.api.DiscountCardService;

@RequiredArgsConstructor
public class DiscountCardServiceImpl implements DiscountCardService {


    private final DiscountCardDao cardDao;
    private final DiscountCardMapper mapper = Mappers.getMapper(DiscountCardMapper.class);

    @Override
    public Page<ResponseDiscountCardDto> findAll(Pageable pageable) {
        Page<MarketDiscountCard> cardPage = cardDao.findAll(pageable);
        return new PageImpl<>(
                cardPage.page() + 1,
                cardPage.size(),
                cardPage.maxPages(),
                cardPage.content().stream()
                        .map(mapper::cardToDto)
                        .toList());
    }

    @Override
    public ResponseDiscountCardDto getById(Integer id) {
        MarketDiscountCard discountCard = cardDao.getById(id)
                .orElseThrow(() -> new DiscountCardNotFoundException(id));
        return mapper.cardToDto(discountCard);
    }

    @Override
    public void update(Integer id, RequestDiscountCardDto dto) {
        checkDiscount(dto);
        MarketDiscountCard discountCard = mapper.dtoToCard(dto);
        discountCard.setId(id);
        cardDao.save(discountCard);
    }

    @Override
    public void delete(Integer id) {
        cardDao.delete(id);
    }

    @Override
    public Integer save(RequestDiscountCardDto dto) {
        checkDiscount(dto);
        MarketDiscountCard discountCard = mapper.dtoToCard(dto);
        return cardDao.save(discountCard);
    }

    private void checkDiscount(RequestDiscountCardDto dto) {
        Byte discount = dto.discount();
        if (discount < 0 || discount > 100) {
            throw new DiscountException();
        }
    }
}
