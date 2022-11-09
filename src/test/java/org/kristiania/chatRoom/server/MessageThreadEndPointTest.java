package org.kristiania.chatRoom.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kristiania.chatRoom.MessageThread;
import org.kristiania.chatRoom.User;
import org.kristiania.chatRoom.database.InMemoryDataSource;
import org.kristiania.chatRoom.database.SampleData;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageThreadEndPointTest {
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
    void shouldAddAndListThread() throws IOException {
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


        //THREAD
        MessageThread thread = SampleData.createSampleThread();
        String threadJson = mapper.writeValueAsString(thread);


        var threadPostConnection = openConnection("/api/thread/1");
        threadPostConnection.setRequestMethod("POST");
        threadPostConnection.setRequestProperty("Content-Type", "application/json");
        threadPostConnection.setDoOutput(true);
        threadPostConnection.getOutputStream().write(threadJson.getBytes(StandardCharsets.UTF_8));

        assertThat(threadPostConnection.getResponseCode())
                .as(threadPostConnection.getResponseMessage() + " for " + threadPostConnection.getURL())
                .isEqualTo(204);

        var connection = openConnection("/api/thread");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);

        assertThat(connection.getInputStream()).asString(StandardCharsets.UTF_8)
                .contains("""
                        "creator":{"dateOfBirth":"2012-01-20","firstName":"Bob","gender":"male","id":1,"lastName":"Kåre","username":"Lulu"},"id":1""");
    }

    private HttpURLConnection openConnection(String spec) throws IOException {
        return (HttpURLConnection) new URL(server.getURL(), spec).openConnection();
    }

}
