package org.kristiania.chatRoom.database;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;

public class InMemoryDataSource {
    public static JdbcDataSource createTestDataSource() {
        var datasource = new JdbcDataSource();
        datasource.setURL("jdbc:h2:mem:testDatabase;DB_CLOSE_DELAY=-1;MODE=LEGACY");

        var flyway = Flyway.configure().dataSource(datasource).load();
        flyway.migrate();
        return datasource;
    }
}
