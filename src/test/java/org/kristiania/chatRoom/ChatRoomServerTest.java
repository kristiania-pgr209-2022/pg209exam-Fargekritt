package org.kristiania.chatRoom;

import jakarta.json.Json;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kristiania.chatRoom.database.InMemoryDataSource;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;


public class ChatRoomServerTest {


    private ChatRoomServer server;

    @BeforeEach
    void setUp() throws Exception {
        server = new ChatRoomServer(0, InMemoryDataSource.createTestDataSource());
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
    void shouldAddAndListUser() throws IOException {
        var postConnection = openConnection("/api/users");
        postConnection.setRequestMethod("POST");
        postConnection.setRequestProperty("Content-Type", "application/json");
        postConnection.setDoOutput(true);
        postConnection.getOutputStream().write(
                Json.createObjectBuilder()
                        .add("username", "TestUser")
                        .add("firstName", "Firsty")
                        .add("lastName", "Lasty")
                        .add("gender", "male")
                       /* .add("dateOfBirth", Json.createObjectBuilder()
                                .add("year", "1999")
                                .add("month","01")
                                .add("dayOfMonth","01"))*/
                        .build().toString().getBytes(StandardCharsets.UTF_8)
        );

        assertThat(postConnection.getResponseCode())
                .as(postConnection.getResponseMessage() + " for " + postConnection.getURL())
                .isEqualTo(204);

        var connection = openConnection("/api/users");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);

        //TODO:
        // ID is 2 when running all tests, but 1 if we only run this test? why tho!?
        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains(""" 
                        firstName":"Firsty","gender":"male""");

    }

/*    @Test
    void shouldGetUserByUserName(){

    }*/
    private HttpURLConnection openConnection(String spec) throws IOException {
        return (HttpURLConnection) new URL(server.getURL(), spec).openConnection();
    }
}
