package ru.clevertec.market.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.market.data.card.response.ResponseDiscountCardDto;
import ru.clevertec.market.data.product.request.RequestProductDto;
import ru.clevertec.market.data.product.response.ResponseProductDto;
import ru.clevertec.market.entity.MarketDiscountCard;
import ru.clevertec.market.entity.MarketProduct;

@Mapper
public interface ReceiptServiceMapper {

    MarketDiscountCard dtoToEntity(ResponseDiscountCardDto dto);

    MarketProduct dtoToEntity(ResponseProductDto dto);

    @Mapping(target = "isDiscount", source = "discount")
    RequestProductDto entityToDto(MarketProduct dto);
}
