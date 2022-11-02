package org.kristiania.chatRoom;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kristiania.chatRoom.database.InMemoryDataSource;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

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

        ObjectMapper mapper = new ObjectMapper();
        User user = sampleUser();
        String Userjson = mapper.writeValueAsString(user);

        System.out.println(Userjson);

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

        var connection = openConnection("/api/users");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);


        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains(""" 
                        dateOfBirth":"2012-01-20","firstName":"Bob","gender":"male""")
                .contains("""
                        lastName":"Kåre","messages":[],"username":"Lulu""");
    }


/*    @Test
    void shouldGetUserByUserName(){
    
        }*/
    private HttpURLConnection openConnection(String spec) throws IOException {
        return (HttpURLConnection) new URL(server.getURL(), spec).openConnection();
    }


    private User sampleUser() {
        var user = new User();
        user.setUsername("Lulu");
        user.setFirstName("Bob");
        user.setLastName("Kåre");
        user.setGender("male");
        user.setDateOfBirth(LocalDate.of(2012, 1, 20));
        return user;
    }
}
