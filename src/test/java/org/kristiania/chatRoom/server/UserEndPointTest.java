package org.kristiania.chatRoom.server;

import org.junit.jupiter.api.Test;
import org.kristiania.chatRoom.User;
import org.kristiania.chatRoom.database.SampleData;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class UserEndPointTest extends AbstractServerTest {


    @Test
    void shouldGetUserById() throws IOException {

        // Set up connection for POST request.
        var postConnection = getPostConnection("api/users");

        // Create sample user, map it to JSON format and write it to the outputStream.
        var user = SampleData.createSampleUser(1);
        String userOneJson = mapper.writeValueAsString(user);
        postConnection.getOutputStream().write(userOneJson.getBytes(StandardCharsets.UTF_8));

        // "Commit" the connection.
        assertThat(postConnection.getResponseCode())
                .as(postConnection.getResponseMessage() + " for " + postConnection.getURL())
                .isEqualTo(204);

        // Set up connection for POST request.
        postConnection = getPostConnection("api/users");

        // Create sample user, map it to JSON format and write it to the outputStream.
        var userTwo = SampleData.createSampleUser(2);
        String userTwoJson = mapper.writeValueAsString(userTwo);
        postConnection.getOutputStream().write(userTwoJson.getBytes(StandardCharsets.UTF_8));

        // "Commit" the connection.
        assertThat(postConnection.getResponseCode())
                .as(postConnection.getResponseMessage() + " for " + postConnection.getURL())
                .isEqualTo(204);

        // Set up connection for GET request and check first sampleUser.
        var connection = openConnection("/api/users/1");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);


        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains(""" 
                        dateOfBirth":"2012-01-20","firstName":"Bob","gender":"male","id":1,"lastName":"Kåre","username":"Lulu""");

        // Set up connection for GET request and check second sampleUser.
        connection = openConnection("/api/users/2");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);


        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains(""" 
                        dateOfBirth":"2011-12-20","firstName":"exampleFirstName","gender":"male","id":2,"lastName":"exampleLastName","username":"exampleUser""");

    }


    @Test
    void shouldAddAndListUser() throws IOException {

        User user = SampleData.createSampleUser(1);
        String userJson = mapper.writeValueAsString(user);


        HttpURLConnection postConnection = getPostConnection("api/users");
        postConnection.getOutputStream().write(userJson.getBytes(StandardCharsets.UTF_8));


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

    @Test
    void shouldListAllUsers() throws IOException {

        // -FIRST USER-

        // Set up connection for POST request.
        var postConnection = getPostConnection("api/users");

        // Create sample user, map it to JSON format and write it to the outputStream.
        var user = SampleData.createSampleUser(1);
        String userOneJson = mapper.writeValueAsString(user);
        postConnection.getOutputStream().write(userOneJson.getBytes(StandardCharsets.UTF_8));

        // "Commit" the connection.
        assertThat(postConnection.getResponseCode())
                .as(postConnection.getResponseMessage() + " for " + postConnection.getURL())
                .isEqualTo(204);

        // -SECOND USER-

        // Set up connection for POST request.
         postConnection = getPostConnection("api/users");

        // Create sample user, map it to JSON format and write it to the outputStream.
        var userTwo = SampleData.createSampleUser(2);
        String userTwoJson = mapper.writeValueAsString(userTwo);
        postConnection.getOutputStream().write(userTwoJson.getBytes(StandardCharsets.UTF_8));

        // "Commit" the connection.
        assertThat(postConnection.getResponseCode())
                .as(postConnection.getResponseMessage() + " for " + postConnection.getURL())
                .isEqualTo(204);

        // -THIRD USER-

        // Set up connection for POST request.
         postConnection = getPostConnection("api/users");

        // Create sample user, map it to JSON format and write it to the outputStream.
        var userThree = SampleData.createSampleUser(3);
        String userThreeJson = mapper.writeValueAsString(userThree);
        postConnection.getOutputStream().write(userThreeJson.getBytes(StandardCharsets.UTF_8));

        // "Commit" the connection.
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
                        dateOfBirth":"2012-01-20","firstName":"Bob","gender":"male","id":1,"lastName":"Kåre","username":"Lulu""")
                .contains("""
                        dateOfBirth":"2011-12-20","firstName":"exampleFirstName","gender":"male","id":2,"lastName":"exampleLastName","username":"exampleUser""")
                .contains("""
                        dateOfBirth":"2010-10-20","firstName":"exampleFirstName2","gender":"male","id":3,"lastName":"exampleLastName2","username":"exampleUser2""");
    }
}
