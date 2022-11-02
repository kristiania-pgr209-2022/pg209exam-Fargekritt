package org.kristiania.store;

import jakarta.json.Json;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kristiania.store.database.InMemoryDataSource;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;


public class ShopServerTest {


    private ShopServer server;

    @BeforeEach
    void setUp() throws Exception {
        server = new ShopServer(0, InMemoryDataSource.createTestDataSource());
        server.start();
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


    @Test
    void shouldAddAndListItem() throws IOException {
        var postConnection = openConnection("/api/items");
        postConnection.setRequestMethod("POST");
        postConnection.setRequestProperty("Content-Type", "application/json");
        postConnection.setDoOutput(true);
        postConnection.getOutputStream().write(
                Json.createObjectBuilder()
                        .add("name", "TestItem")
                        .add("artNumber", "0000-0000")
                        .add("category", "TestCategory")
                        .add("description", "This is a test item for testing post")
                        .build().toString().getBytes(StandardCharsets.UTF_8)
        );

        assertThat(postConnection.getResponseCode())
                .as(postConnection.getResponseMessage() + " for " + postConnection.getURL())
                .isEqualTo(204);

        var connection = openConnection("/api/items");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);

        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("\"name\":\"TestItem\"");
    }

    private HttpURLConnection openConnection(String spec) throws IOException {
        return (HttpURLConnection) new URL(server.getURL(), spec).openConnection();
    }
}
