package ru.clevertec.market.service;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import ru.clevertec.market.data.Basket;
import ru.clevertec.market.data.MarketBasket;
import ru.clevertec.market.data.Order;
import ru.clevertec.market.data.OrderEntry;
import ru.clevertec.market.data.card.response.ResponseDiscountCardDto;
import ru.clevertec.market.data.product.response.ResponseProductDto;
import ru.clevertec.market.data.receipt.Receipt;
import ru.clevertec.market.entity.MarketDiscountCard;
import ru.clevertec.market.entity.MarketProduct;
import ru.clevertec.market.exception.ProductQuantityIsNotAvailableException;
import ru.clevertec.market.factory.ReceiptFactory;
import ru.clevertec.market.mapper.ReceiptServiceMapper;
import ru.clevertec.market.service.api.DiscountCardService;
import ru.clevertec.market.service.api.ProductService;
import ru.clevertec.market.service.api.ReceiptService;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService {

    private final ProductService productService;
    private final DiscountCardService cardService;
    private final ReceiptFactory receiptFactory;
    private final ReceiptServiceMapper mapper = Mappers.getMapper(ReceiptServiceMapper.class);
    private final Lock lock = new ReentrantLock();

    @Override
    public Receipt getReceipt(Order order) {
        final MarketDiscountCard discountCard = Optional.ofNullable(order.discountCardNumber())
                .map(cardService::getById)
                .map(mapper::dtoToEntity)
                .orElse(null);
        final Basket basket = new MarketBasket();

        lock.lock();
        try {
            order.orderEntries().stream()
                    .map(orderEntry -> {
                        ResponseProductDto productDto = productService.getById(orderEntry.id());
                        int updatedQuantity = productDto.quantity() - orderEntry.quantity();
                        if (updatedQuantity < 0) {
                            throw new ProductQuantityIsNotAvailableException(orderEntry.id(), orderEntry.quantity());
                        }

                        MarketProduct product = mapper.dtoToEntity(productDto);
                        product.setQuantity(updatedQuantity);

                        basket.addProduct(product, orderEntry.quantity());
                        return product;
                    }).toList()
                    .forEach(product -> productService.update(product.getId(), mapper.entityToDto(product)));
        } finally {
            lock.unlock();
        }
        return receiptFactory.create(basket, discountCard, 1234);
    }
}
