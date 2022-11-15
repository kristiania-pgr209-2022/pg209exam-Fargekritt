package org.kristiania.chatRoom.database;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.SQLException;

public class InMemoryDataSource {

    private static JdbcDataSource datasource;

    public static JdbcDataSource createTestDataSource(String databaseName) {
        datasource = new JdbcDataSource();
        datasource.setURL("jdbc:h2:mem:" + databaseName + ";DB_CLOSE_DELAY=-1;MODE=LEGACY");

        var flyway = Flyway.configure().dataSource(datasource).cleanDisabled(false).load();
        flyway.clean();
        flyway.migrate();
        return datasource;
    }
}
