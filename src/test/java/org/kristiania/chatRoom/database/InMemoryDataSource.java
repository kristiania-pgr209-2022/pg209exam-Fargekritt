package org.kristiania.chatRoom.database;

import org.flywaydb.core.Flyway;
import org.h2.jdbcx.JdbcDataSource;

import java.sql.SQLException;

public class InMemoryDataSource {

    private static JdbcDataSource datasource;

    public static JdbcDataSource createTestDataSource() {
        datasource = new JdbcDataSource();
        datasource.setURL("jdbc:h2:mem:testDatabase;DB_CLOSE_DELAY=-1;MODE=LEGACY");

        var flyway = Flyway.configure().dataSource(datasource).load();
        flyway.migrate();
        return datasource;
    }

    public static void clearTestDataSource() throws SQLException {
        datasource.getConnection().createStatement().execute("DELETE FROM messages");
        datasource.getConnection().createStatement().execute("DELETE FROM threads");
        datasource.getConnection().createStatement().execute("DELETE FROM users");
        datasource.getConnection().createStatement().execute("ALTER TABLE messages ALTER COLUMN id RESTART WITH 1");
        datasource.getConnection().createStatement().execute("ALTER TABLE threads ALTER COLUMN id RESTART WITH 1");
        datasource.getConnection().createStatement().execute("ALTER TABLE users ALTER COLUMN id RESTART WITH 1");
//        datasource.getConnection().createStatement().execute("DELETE FROM *");
//        datasource.getConnection().createStatement().execute("ALTER TABLE * ALTER COLUMN id RESTART WITH 1");
    }
}
