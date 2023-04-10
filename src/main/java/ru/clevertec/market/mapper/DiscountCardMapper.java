package ru.clevertec.market.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.clevertec.market.data.card.request.RequestDiscountCardDto;
import ru.clevertec.market.data.card.response.ResponseDiscountCardDto;
import ru.clevertec.market.entity.MarketDiscountCard;

import java.sql.ResultSet;
import java.sql.SQLException;

@Mapper
public interface DiscountCardMapper {

    @Mapping(target = "id", ignore = true)
    MarketDiscountCard dtoToCard(RequestDiscountCardDto dto);

    ResponseDiscountCardDto cardToDto(MarketDiscountCard card);

    @Mapping(target = "id", expression = "java(resultSet.getInt(\"id\"))")
    @Mapping(target = "discount", expression = "java(resultSet.getByte(\"discount\"))")
    MarketDiscountCard resultSetToCard(ResultSet resultSet) throws SQLException;
}
