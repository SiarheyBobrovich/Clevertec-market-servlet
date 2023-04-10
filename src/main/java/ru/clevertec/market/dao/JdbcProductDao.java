package ru.clevertec.market.dao;

import lombok.RequiredArgsConstructor;
import org.mapstruct.factory.Mappers;
import ru.clevertec.market.dao.api.ProductDao;
import ru.clevertec.market.entity.MarketProduct;
import ru.clevertec.market.exception.ProductSqlException;
import ru.clevertec.market.mapper.ProductMapper;
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
import java.util.Optional;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static ru.clevertec.market.dao.util.ProductQueryUtil.*;

@RequiredArgsConstructor
public class JdbcProductDao implements ProductDao {

    private final DataSource dataSource;
    private final ProductMapper mapper = Mappers.getMapper(ProductMapper.class);

    @Override
    public Optional<MarketProduct> findById(Integer id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_PRODUCT_BY_ID_QUERY)
        ) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? Optional.of(mapper.resultSetToProduct(resultSet)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new ProductSqlException();
        }
    }

    @Override
    public boolean exists(Integer id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_PRODUCT_BY_ID_QUERY)
        ) {
            statement.setInt(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new ProductSqlException();
        }
    }

    @Override
    public boolean isExistsAndQuantityAvailable(Integer id, Integer quantity) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_PRODUCT_BY_ID_AND_QUANTITY_QUERY)
        ) {
            statement.setInt(1, id);
            statement.setInt(2, quantity);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        } catch (SQLException e) {
            throw new ProductSqlException();
        }
    }

    @Override
    public void update(MarketProduct product) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE_PRODUCT_QUERY)
        ) {
            statement.setString(1, product.getDescription());
            statement.setBigDecimal(2, product.getPrice());
            statement.setInt(3, product.getQuantity());
            statement.setBoolean(4, product.isDiscount());
            statement.setInt(5, product.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ProductSqlException();
        }
    }

    @Override
    public void delete(Integer id) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE_PRODUCT_BY_ID)
        ) {
            statement.setInt(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new ProductSqlException();
        }
    }

    @Override
    public Integer save(MarketProduct product) {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     INSERT_NEW_PRODUCT, RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, product.getDescription());
            statement.setBigDecimal(2, product.getPrice());
            statement.setInt(3, product.getQuantity());
            statement.setBoolean(4, product.isDiscount());

            int count = statement.executeUpdate();
            if (count == 0) {
                throw new ProductSqlException("Creating product failed, no rows affected.");
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();
            if (!generatedKeys.next()) {
                throw new ProductSqlException("Creating product failed, no ID obtained.");
            }
            int id = generatedKeys.getInt("id");
            product.setId(id);

            return id;
        } catch (SQLException e) {
            throw new ProductSqlException();
        }
    }

    @Override
    public List<MarketProduct> findAll() {
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT_ALL_PRODUCTS)
        ) {
            try (ResultSet resultSet = statement.executeQuery()) {
                final List<MarketProduct> products = new ArrayList<>();
                while (resultSet.next()) {
                    products.add(mapper.resultSetToProduct(resultSet));
                }
                return products;
            }
        } catch (SQLException e) {
            throw new ProductSqlException();
        }
    }

    @Override
    public Page<MarketProduct> findAll(Pageable pageable) {
        final List<MarketProduct> products = new ArrayList<>();
        final int rows;
        int page = pageable.page();
        int size = pageable.size();
        try (Connection connection = dataSource.getConnection();
             PreparedStatement productsStatement = connection.prepareStatement(SELECT_ALL_PRODUCTS_PAGEABLE);
             PreparedStatement rowStatement = connection.prepareStatement(SELECT_ROW_COUNT)
        ) {
            productsStatement.setInt(1, page * size);
            productsStatement.setInt(2, size);
            try (ResultSet resultSet = productsStatement.executeQuery()) {
                while (resultSet.next()) {
                    products.add(mapper.resultSetToProduct(resultSet));
                }
            }
            try (ResultSet resultSet = rowStatement.executeQuery()) {
                resultSet.next();
                rows = resultSet.getInt(1);
            }

            return new PageImpl<>(
                    page,
                    size,
                    rows / size + (rows % size == 0 ? 0 : 1),
                    products);
        } catch (SQLException e) {
            throw new ProductSqlException();
        }
    }
}
