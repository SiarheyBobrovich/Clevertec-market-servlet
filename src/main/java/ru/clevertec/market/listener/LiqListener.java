package ru.clevertec.market.listener;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import liquibase.CatalogAndSchema;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.ClassLoaderResourceAccessor;
import org.apache.commons.configuration2.Configuration;
import ru.clevertec.market.config.ApplicationConfig;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

@WebListener
public class LiqListener implements ServletContextListener {

    private static final String LIQUIBASE_CHANGE_LOG_FILE = "liquibase.changeLog.file";
    private static final String LIQUIBASE_CHANGE_LOG_SCHEMA = "liquibase.changeLog.schema";
    private static final String DATASOURCE_DDL_AUTO = "datasource.ddl.auto";

    private final Configuration configuration;

    public LiqListener() {
        configuration = ApplicationConfig.getConfiguration();
    }

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        final String ddlOption;
        ddlOption = configuration.getString(DATASOURCE_DDL_AUTO);
        if (Objects.nonNull(ddlOption)) {
            try {
                init(ddlOption);
            } catch (SQLException | LiquibaseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void init(String option) throws SQLException, LiquibaseException {
        String schemaName = configuration.getString(LIQUIBASE_CHANGE_LOG_SCHEMA, "public");
        String createSchema = String.format("CREATE SCHEMA IF NOT EXISTS %s;", schemaName);
        String changelogFileName = configuration.getString(LIQUIBASE_CHANGE_LOG_FILE);

        Liquibase liquibase = null;
        Database database = null;
        Connection connection = null;
        try {
            connection = ApplicationConfig.getDataSource()
                    .getConnection();
            database = DatabaseFactory.getInstance()
                    .findCorrectDatabaseImplementation(new JdbcConnection(connection));
            try (PreparedStatement preparedStatement = connection.prepareStatement(createSchema)) {
                preparedStatement.executeUpdate();
                connection.commit();
            }
            database.setDefaultSchemaName(schemaName);
            liquibase = new Liquibase(changelogFileName,
                    new ClassLoaderResourceAccessor(), database);

            if ("drop-create".equals(option)) {
                liquibase.dropAll(CatalogAndSchema.DEFAULT);
                liquibase.update();
            } else if ("create".equals(option)) {
                liquibase.update();
            }
        } catch (SQLException | LiquibaseException e) {
            if (liquibase != null) {
                liquibase.close();
            }
            if (database != null) {
                database.close();
            }
            if (connection != null) {
                connection.close();
            }
            throw new RuntimeException(e);
        }
    }
}
