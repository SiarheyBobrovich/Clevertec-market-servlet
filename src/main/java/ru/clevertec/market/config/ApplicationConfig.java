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

public class ApplicationConfig {

    private static final String APPLICATION_YML = "/application.yml";
    private static final String DATASOURCE_URL ="datasource.url";
    private static final String DATASOURCE_USERNAME = "datasource.username";
    private static final String DATASOURCE_PASSWORD = "datasource.password";

    private static final Configuration CONFIGURATION;
    private static final DataSource DATA_SOURCE;
    private static final ProductDao PRODUCT_DAO;
    private static final ProductService PRODUCT_SERVICE;
    private static final ObjectMapper OBJECT_MAPPER;
    private static final DiscountCardDao DISCOUNT_CARD_DAO;
    private static final DiscountCardService DISCOUNT_CARD_SERVICE;

    static {
        final YAMLConfiguration yamlConfiguration = new YAMLConfiguration();
        try {
            yamlConfiguration.read(ApplicationConfig.class.getResourceAsStream(APPLICATION_YML));
        } catch (ConfigurationException e) {
            throw new RuntimeException(e);
        }
        CONFIGURATION = yamlConfiguration;
    }

    static {
        PGSimpleDataSource pgSimpleDataSource = new PGSimpleDataSource();
        pgSimpleDataSource.setURL(CONFIGURATION.getString(DATASOURCE_URL));
        pgSimpleDataSource.setUser(CONFIGURATION.getString(DATASOURCE_USERNAME));
        pgSimpleDataSource.setPassword(CONFIGURATION.getString(DATASOURCE_PASSWORD));
        DATA_SOURCE = pgSimpleDataSource;
    }

    static {
        PRODUCT_DAO = new JdbcProductDao(DATA_SOURCE);
        DISCOUNT_CARD_DAO = new JdbcDiscountCardDao(DATA_SOURCE);
        PRODUCT_SERVICE = new ProductServiceImpl(PRODUCT_DAO);
        DISCOUNT_CARD_SERVICE = new DiscountCardServiceImpl(DISCOUNT_CARD_DAO);
        OBJECT_MAPPER = new ObjectMapper().setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    private ApplicationConfig() {
        throw new UnsupportedOperationException("Config class");
    }

    public static ProductService getProductService() {
        return PRODUCT_SERVICE;
    }

    public static DiscountCardService getDiscountCardService() {
        return DISCOUNT_CARD_SERVICE;
    }

    public static ObjectMapper getObjectMapper() {
        return OBJECT_MAPPER;
    }

    public static DataSource getDataSource() {
        return DATA_SOURCE;
    }
    public static Configuration getConfiguration() {
        return CONFIGURATION;
    }
}
