package org.kristiania.chatRoom.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kristiania.chatRoom.User;
import org.kristiania.chatRoom.database.InMemoryDataSource;
import org.kristiania.chatRoom.database.SampleData;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class UserEndPointTest {
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
    void shouldGetUserById() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        User user = SampleData.createSampleUser();
        user.setUsername("Fargekritt");
        String Userjson = mapper.writeValueAsString(user);

        var postConnection = openConnection("/api/users");
        postConnection.setRequestMethod("POST");
        postConnection.setRequestProperty("Content-Type", "application/json");
        postConnection.setDoOutput(true);
        postConnection.getOutputStream().write(Userjson.getBytes(StandardCharsets.UTF_8));

        assertThat(postConnection.getResponseCode())
                .as(postConnection.getResponseMessage() + " for " + postConnection.getURL())
                .isEqualTo(204);


        var connection = openConnection("/api/users/1");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);


        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains(""" 
                        dateOfBirth":"2012-01-20","firstName":"Bob","gender":"male","id":1,"lastName":"Kåre","username":"Fargekritt""");


    }


    @Test
    void shouldAddAndListUser() throws IOException {

        ObjectMapper mapper = new ObjectMapper();
        User user = SampleData.createSampleUser();
        String Userjson = mapper.writeValueAsString(user);


        var postConnection = openConnection("/api/users");
        postConnection.setRequestMethod("POST");
        postConnection.setRequestProperty("Content-Type", "application/json");
        postConnection.setDoOutput(true);
        postConnection.getOutputStream().write(Userjson.getBytes(StandardCharsets.UTF_8));


//        // Old way to send json.
//       postConnection.getOutputStream().write(
//                Json.createObjectBuilder()
//                        .add("username", "TestUser")
//                        .add("firstName", "Firsty")
//                        .add("lastName", "Lasty")
//                        .add("gender", "male")
//                        .add("dateOfBirth","2012-01-20")
//                        .build().toString().getBytes(StandardCharsets.UTF_8));
//


        assertThat(postConnection.getResponseCode())
                .as(postConnection.getResponseMessage() + " for " + postConnection.getURL())
                .isEqualTo(204);

        var connection = openConnection("/api/users/");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);


        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("""
                        dateOfBirth":"2012-01-20","firstName":"Bob","gender":"male","id":1,"lastName":"Kåre","username":"Lulu""");
    }


    private HttpURLConnection openConnection(String spec) throws IOException {
        return (HttpURLConnection) new URL(server.getURL(), spec).openConnection();
    }

}
