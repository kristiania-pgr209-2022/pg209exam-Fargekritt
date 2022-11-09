package org.kristiania.chatRoom.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kristiania.chatRoom.Message;
import org.kristiania.chatRoom.User;
import org.kristiania.chatRoom.database.InMemoryDataSource;
import org.kristiania.chatRoom.database.SampleData;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageEndPointTest {

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
    void shouldAddAndListAllMessages() throws IOException {

        ObjectMapper mapper = new ObjectMapper();

        //USER
        User user = SampleData.createSampleUser();
        String userJson = mapper.writeValueAsString(user);

        var userPostConnection = openConnection("/api/users");
        userPostConnection.setRequestMethod("POST");
        userPostConnection.setRequestProperty("Content-Type", "application/json");
        userPostConnection.setDoOutput(true);
        userPostConnection.getOutputStream().write(userJson.getBytes(StandardCharsets.UTF_8));

        assertThat(userPostConnection.getResponseCode())
                .as(userPostConnection.getResponseMessage() + " for " + userPostConnection.getURL())
                .isEqualTo(204);

        //Message
        Message message = SampleData.createSampleMessage();
        String messageJson = mapper.writeValueAsString(message);

        var messagePostConnection = openConnection("/api/messages/user/1");
        messagePostConnection.setRequestMethod("POST");
        messagePostConnection.setRequestProperty("Content-Type", "application/json");
        messagePostConnection.setDoOutput(true);
        messagePostConnection.getOutputStream().write(messageJson.getBytes(StandardCharsets.UTF_8));


        assertThat(messagePostConnection.getResponseCode())
                .as(messagePostConnection.getResponseMessage() + " for " + messagePostConnection.getURL())
                .isEqualTo(204);

        var connection = openConnection("/api/messages/");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);


        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("""
                        body":"This is a testing body for a message""")
                .contains("""
                        firstName":"Bob""");
    }

    private HttpURLConnection openConnection(String spec) throws IOException {
        return (HttpURLConnection) new URL(server.getURL(), spec).openConnection();
    }
}
