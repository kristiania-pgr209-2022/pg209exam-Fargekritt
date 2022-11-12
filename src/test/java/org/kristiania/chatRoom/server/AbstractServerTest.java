package org.kristiania.chatRoom.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.kristiania.chatRoom.database.InMemoryDataSource;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public abstract class AbstractServerTest {

    private ChatRoomServer server;
    protected ObjectMapper mapper = new ObjectMapper();

    @BeforeEach
    void setUp() throws Exception {
        var dataSource = InMemoryDataSource.createTestDataSource();
        InMemoryDataSource.clearTestDataSource();

        server = new ChatRoomServer(0, dataSource);
        server.start();
    }

    @AfterEach
    void tearDown() throws SQLException {
        InMemoryDataSource.clearTestDataSource();
    }

    protected HttpURLConnection getPostConnection(String url) throws IOException {
        var postConnection = openConnection(url);
        postConnection.setRequestMethod("POST");
        postConnection.setRequestProperty("Content-Type", "application/json");
        postConnection.setDoOutput(true);
        return postConnection;
    }

    protected void doPostRequest(String url, String json) throws IOException {


            // Set up connection for POST request.
            var postConnection = getPostConnection(url);

            // Write the json to the outputStream.
            postConnection.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));

            // "Commit" the connection.
            assertThat(postConnection.getResponseCode())
                    .as(postConnection.getResponseMessage() + " for " + postConnection.getURL())
                    .isEqualTo(204);

    }

    protected HttpURLConnection openConnection(String spec) throws IOException {
        return (HttpURLConnection) new URL(server.getURL(), spec).openConnection();
    }
}
