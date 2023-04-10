package ru.clevertec.market.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import org.apache.commons.configuration2.Configuration;
import ru.clevertec.market.config.ApplicationConfig;
import ru.clevertec.market.dao.JdbcInitializer;

import java.util.Objects;

public class JdbcContextListener implements ServletContextListener {

    private static final String DATASOURCE_DDL_AUTO = "datasource.ddl.auto";
    private final Configuration configuration;

    public JdbcContextListener() {
        configuration = ApplicationConfig.getConfiguration();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final String ddlOption = configuration.getString(DATASOURCE_DDL_AUTO);
        if (Objects.nonNull(ddlOption)) {
            final JdbcInitializer jdbcInitializer = new JdbcInitializer(ApplicationConfig.getDataSource());
            switch (ddlOption) {
                case "create-only" -> jdbcInitializer.create();
                case "create" -> jdbcInitializer.dropCreate();
                case "create-insert" -> jdbcInitializer.createInsert();
                default -> throw new IllegalStateException("Unexpected value: " + ddlOption);
            }
        }
    }
}
