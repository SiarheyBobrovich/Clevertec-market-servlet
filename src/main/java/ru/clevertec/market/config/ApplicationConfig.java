package ru.clevertec.market.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import org.apache.commons.configuration2.Configuration;
import org.apache.commons.configuration2.YAMLConfiguration;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.postgresql.ds.PGSimpleDataSource;
import ru.clevertec.market.dao.JdbcDiscountCardDao;
import ru.clevertec.market.dao.JdbcProductDao;
import ru.clevertec.market.dao.api.DiscountCardDao;
import ru.clevertec.market.dao.api.ProductDao;
import ru.clevertec.market.service.DiscountCardServiceImpl;
import ru.clevertec.market.service.ProductServiceImpl;
import ru.clevertec.market.service.api.DiscountCardService;
import ru.clevertec.market.service.api.ProductService;

import javax.sql.DataSource;
import java.io.InputStream;
import java.util.TimeZone;

public class ApplicationConfig {

    private static final String APPLICATION_YML = "/application.yml";
    private static final String DATASOURCE_URL = "datasource.url";
    private static final String DATASOURCE_USERNAME = "datasource.username";
    private static final String DATASOURCE_PASSWORD = "datasource.password";

    private static final DataSource dataSource;
    private static final Configuration configuration;
    private static final ProductDao productDao;
    private static final ProductService productService;
    private static final ObjectMapper objectMapper;
    private static final DiscountCardDao discountCardDao;
    private static final DiscountCardService cardService;

    static {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    static {
        final InputStream resourceAsStream = ApplicationConfig.class.getResourceAsStream(APPLICATION_YML);
        final YAMLConfiguration yamlConfiguration = new YAMLConfiguration();
        try {
            yamlConfiguration.read(resourceAsStream);
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
        configuration = yamlConfiguration;
    }

    static {
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setURL(configuration.getString(DATASOURCE_URL));
        pgSimpleDataSource.setUser(configuration.getString(DATASOURCE_USERNAME));
        pgSimpleDataSource.setPassword(configuration.getString(DATASOURCE_PASSWORD));
        dataSource = pgSimpleDataSource;
    }

    static {
        productDao = new JdbcProductDao(dataSource);
        discountCardDao = new JdbcDiscountCardDao(dataSource);
        productService = new ProductServiceImpl(productDao);
        cardService = new DiscountCardServiceImpl(discountCardDao);
        objectMapper = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
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

    public static Configuration getConfiguration() {
        return configuration;
    }

    public static DataSource getDataSource() {
        return dataSource;
    }
}
