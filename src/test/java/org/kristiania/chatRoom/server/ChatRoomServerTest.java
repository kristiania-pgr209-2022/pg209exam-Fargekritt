package org.kristiania.chatRoom.server;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kristiania.chatRoom.database.InMemoryDataSource;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;


public class ChatRoomServerTest {

    private ChatRoomServer server;

    @BeforeEach
    void setUp() throws Exception {
        var dataSource = InMemoryDataSource.createTestDataSource();
        server = new ChatRoomServer(0, dataSource);
        server.start();
    }

    @AfterEach
    void tearDown() throws SQLException {
        InMemoryDataSource.clearTestDataSource();
    }



    @Test
    void shouldServeHomepage() throws Exception {
        var connection = openConnection("/");

        assertThat(connection.getResponseCode())
                .as(connection.getResponseCode() + " For " + connection.getURL())
                .isEqualTo(200);

        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("<title>Ultra Shop</title>");

    }


    private HttpURLConnection openConnection(String spec) throws IOException {
        return (HttpURLConnection) new URL(server.getURL(), spec).openConnection();
    }

}
