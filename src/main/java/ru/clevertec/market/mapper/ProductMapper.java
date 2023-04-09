package ru.clevertec.market.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import ru.clevertec.market.data.product.request.RequestProductDto;
import ru.clevertec.market.data.product.response.ResponseProductDto;
import ru.clevertec.market.entity.MarketProduct;

import java.sql.ResultSet;
import java.sql.SQLException;

@Mapper
public interface ProductMapper {

    /**
     * Mapping request dto to product
     *
     * @param source current request dto
     * @return product without ID
     */
    @Mapping(target = "id", ignore = true)
    MarketProduct getProduct(RequestProductDto source);

    /**
     * Mapping product to response dto
     *
     * @param source current product
     * @return response dto
     */
    @Mapping(target = "isDiscount", source = "discount")
    ResponseProductDto getResponseDto(MarketProduct source);

    @Mapping(target = "id", expression = "java(resultSet.getInt(\"id\"))")
    @Mapping(target = "description", expression = "java(resultSet.getString(\"description\"))")
    @Mapping(target = "price", expression = "java(resultSet.getBigDecimal(\"price\"))")
    @Mapping(target = "quantity", expression = "java(resultSet.getInt(\"quantity\"))")
    @Mapping(target = "isDiscount", expression = "java(resultSet.getBoolean(\"is_discount\"))")
    MarketProduct resultSetToProduct(ResultSet resultSet) throws SQLException;
}
