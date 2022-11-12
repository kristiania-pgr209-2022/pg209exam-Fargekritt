package org.kristiania.chatRoom.server;
import org.junit.jupiter.api.Test;
import org.kristiania.chatRoom.User;
import org.kristiania.chatRoom.database.SampleData;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class UserEndPointTest extends AbstractServerTest {


    @Test
    void shouldGetUserById() throws IOException {


        // Create sample user, map it to JSON format and send post-url and json to doPostRequest.
        var firstSampleUser = SampleData.createSampleUser(1);
        String firstSampleUserJson = mapper.writeValueAsString(firstSampleUser);
        doPostRequest("api/users",firstSampleUserJson);

        var secondSampleUser = SampleData.createSampleUser(2);
        String secondSampleUserJson = mapper.writeValueAsString(secondSampleUser);
        doPostRequest("api/users",secondSampleUserJson);

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


        var postConnection = getPostConnection("api/users");
        postConnection.getOutputStream().write(userJson.getBytes(StandardCharsets.UTF_8));

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
        // Create sample user, map it to JSON format and send post-url and json to doPostRequest
        var firstSampleUser = SampleData.createSampleUser(1);
        String firstSampleUserJson = mapper.writeValueAsString(firstSampleUser);
        doPostRequest("api/users",firstSampleUserJson);

        var secondSampleUser = SampleData.createSampleUser(2);
        String secondSampleUserJson = mapper.writeValueAsString(secondSampleUser);
        doPostRequest("api/users",secondSampleUserJson);

        var thirdSampleUser = SampleData.createSampleUser(3);
        String thirdSampleUserJson = mapper.writeValueAsString(thirdSampleUser);
        doPostRequest("api/users",thirdSampleUserJson);


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
