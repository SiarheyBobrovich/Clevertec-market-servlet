package ru.clevertec.market.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import ru.clevertec.market.dao.api.ProductDao;
import ru.clevertec.market.data.product.request.RequestProductDto;
import ru.clevertec.market.data.product.response.ResponseProductDto;
import ru.clevertec.market.entity.MarketProduct;
import ru.clevertec.market.exception.ProductNotFoundException;
import ru.clevertec.market.mapper.ProductMapper;
import ru.clevertec.market.pagination.Page;
import ru.clevertec.market.pagination.PageImpl;
import ru.clevertec.market.pagination.Pageable;
import ru.clevertec.market.service.api.ProductService;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductDao productDao;
    private final ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    @Override
    public ResponseProductDto getById(Integer id) {
        Optional<MarketProduct> optionalProduct = productDao.findById(id);
        MarketProduct product = optionalProduct.orElseThrow(() ->
                new ProductNotFoundException(id));
        return mapper.getResponseDto(product);
    }

    @Override
    public List<ResponseProductDto> getAll() {
        return productDao.findAll().stream()
                .map(mapper::getResponseDto)
                .toList();
    }

    @Override
    public Page<ResponseProductDto> getAll(Pageable page) {
        Page<MarketProduct> all = productDao.findAll(page);
        return new PageImpl<>(
                all.page() + 1,
                all.size(),
                all.maxPages(),
                all.content().stream()
                        .map(mapper::getResponseDto)
                        .toList());
    }

    @Override
    public Integer save(RequestProductDto dto) {
        MarketProduct product = mapper.getProduct(dto);
        return productDao.save(product);
    }

    @Override
    public void update(Integer id, RequestProductDto dto) {
        MarketProduct product = mapper.getProduct(dto);
        product.setId(id);
        productDao.update(product);
    }

    @Override
    public void delete(Integer id) {
        productDao.delete(id);
    }
}
