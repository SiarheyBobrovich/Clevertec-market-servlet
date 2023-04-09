package ru.clevertec.market.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import org.apache.commons.configuration2.YAMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.postgresql.ds.PGSimpleDataSource;
import ru.clevertec.market.dao.JdbcDiscountCardDao;
import ru.clevertec.market.dao.JdbcInitializer;
import ru.clevertec.market.dao.JdbcProductDao;
import ru.clevertec.market.dao.api.DiscountCardDao;
import ru.clevertec.market.dao.api.ProductDao;
import ru.clevertec.market.service.DiscountCardServiceImpl;
import ru.clevertec.market.service.PdfServiceImpl;
import ru.clevertec.market.service.ProductServiceImpl;
import ru.clevertec.market.service.api.DiscountCardService;
import ru.clevertec.market.service.api.ProductService;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.Objects;

public class ApplicationConfig {

    private static final DataSource dataSource;
    private static final ProductDao productDao;
    private static final ProductService productService;
    private static final ObjectMapper objectMapper;
    private static final DiscountCardDao discountCardDao;
    private static final DiscountCardService cardService;

    static {
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setServerNames(new String[]{"localhost:5432"});
        pgSimpleDataSource.setDatabaseName("clevertec");
        pgSimpleDataSource.setUser("postgres");
        pgSimpleDataSource.setPassword("172143");

        dataSource = pgSimpleDataSource;
        productDao = new JdbcProductDao(dataSource);
        discountCardDao = new JdbcDiscountCardDao(dataSource);
        productService = new ProductServiceImpl(productDao);
        cardService = new DiscountCardServiceImpl(discountCardDao);
        objectMapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    //initialize db
    static {
        final InputStream resourceAsStream = PdfServiceImpl.class.getResourceAsStream("/application.yml");
        final YAMLConfiguration yamlConfiguration = new YAMLConfiguration();
        final String ddlOption;
        try {
            yamlConfiguration.read(resourceAsStream);
            ddlOption = yamlConfiguration.getString("datasource.ddl.auto");
            if (Objects.nonNull(ddlOption)) {
                final JdbcInitializer jdbcInitializer = new JdbcInitializer(dataSource);
                switch (ddlOption) {
                    case "create-only" -> jdbcInitializer.create();
                    case "create" -> jdbcInitializer.dropCreate();
                    case "create-insert" -> jdbcInitializer.createInsert();
                    default -> throw new IllegalStateException("Unexpected value: " + ddlOption);
                }
            }
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
    }

    private ApplicationConfig() {
        throw new UnsupportedOperationException("Config class");
    }

    public static ProductService getProductService() {
        return productService;
    }

    public static DiscountCardService getCardService() {
        return cardService;
    }

    public static ObjectMapper getObjectMapper() {
        return objectMapper;
    }
}
