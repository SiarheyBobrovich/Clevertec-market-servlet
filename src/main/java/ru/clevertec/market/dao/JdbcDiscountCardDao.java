package ru.clevertec.market.dao;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import ru.clevertec.market.dao.api.DiscountCardDao;
import ru.clevertec.market.entity.MarketDiscountCard;
import ru.clevertec.market.exception.DiscountCardSqlException;
import ru.clevertec.market.mapper.DiscountCardMapper;
import ru.clevertec.market.pagination.Page;
import ru.clevertec.market.pagination.PageImpl;
import ru.clevertec.market.pagination.Pageable;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static ru.clevertec.market.dao.util.DiscountCardQueryUtil.*;

@RequiredArgsConstructor
public class JdbcDiscountCardDao implements DiscountCardDao {

    private final DataSource dataSource;
    private final DiscountCardMapper mapper = Mappers.getMapper(DiscountCardMapper.class);

    @Override
    public Optional<MarketDiscountCard> getById(Integer id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_DISCOUNT_CARD_BY_ID_QUERY)
        ) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(mapper.resultSetToCard(resultSet)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Data base is not available");
        }
    }

    @Override
    public Page<MarketDiscountCard> findAll(Pageable pageable) {
        final List<MarketDiscountCard> cards = new ArrayList<>();
        final int rows;
        int page = pageable.page();
        int size = pageable.size();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement productsStatement = connection.prepareStatement(SELECT_ALL_DISCOUNT_CARDS_PAGEABLE);
             PreparedStatement rowStatement = connection.prepareStatement(SELECT_ROW_COUNT)
        ) {
            productsStatement.setInt(1, page * size);
            productsStatement.setInt(2, size);
            try (ResultSet resultSet = productsStatement.executeQuery()) {
                while (resultSet.next()) {
                    cards.add(mapper.resultSetToCard(resultSet));
                }
            }
            try (ResultSet resultSet = rowStatement.executeQuery()) {
                resultSet.next();
                rows = resultSet.getInt(1);
            }

            return new PageImpl<>(page, size, Page.calculateMaxPages(rows, size), cards);
        } catch (SQLException e) {
            throw new DiscountCardSqlException(e.getMessage());
        }
    }

    @Override
    public Integer save(MarketDiscountCard discountCard) {
        if (Objects.nonNull(discountCard.getId())) {
            return update(discountCard);
        }
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_NEW_DISCOUNT_CARD, RETURN_GENERATED_KEYS)) {
            preparedStatement.setByte(1, discountCard.getDiscount());
            int count = preparedStatement.executeUpdate();
            if (count == 0) {
                throw new DiscountCardSqlException("Creating discount card failed, no rows affected.");
            }
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new DiscountCardSqlException("Creating discount card failed, no ID obtained.");
            }
            int id = generatedKeys.getInt("id");
            discountCard.setId(id);

            return id;
        } catch (SQLException e) {
            throw new DiscountCardSqlException(e.getMessage());
        }
    }

    @Override
    public void delete(Integer id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_DISCOUNT_CARD_BY_ID)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DiscountCardSqlException(e.getMessage());
        }
    }

    private Integer update(MarketDiscountCard discountCard) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_DISCOUNT_CARD_BY_ID)) {
            preparedStatement.setByte(1, discountCard.getDiscount());
            preparedStatement.setInt(2, discountCard.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new DiscountCardSqlException(e.getMessage());
        }
        return discountCard.getId();
    }
}
