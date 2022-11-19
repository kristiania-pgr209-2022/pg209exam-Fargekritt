package org.kristiania.chatRoom.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.kristiania.chatRoom.dto.MemberDto;
import org.kristiania.chatRoom.dto.MessageThreadDto;
import org.kristiania.chatRoom.entities.Message;
import org.kristiania.chatRoom.entities.MessageThread;
import org.kristiania.chatRoom.entities.User;
import org.kristiania.chatRoom.database.SampleData;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserEndPointTest extends AbstractServerTest {


    @Test
    void shouldGetUserById() throws IOException {


        // Create sample user, map it to JSON format and send post-url and json to doPostRequest.
        var firstSampleUser = SampleData.createSampleUser(1);
        String firstSampleUserJson = mapper.writeValueAsString(firstSampleUser);
        doPostRequest("api/users", firstSampleUserJson);

        var secondSampleUser = SampleData.createSampleUser(2);
        String secondSampleUserJson = mapper.writeValueAsString(secondSampleUser);
        doPostRequest("api/users", secondSampleUserJson);

        // Set up connection for GET request and check first sampleUser.
        var connection = openConnection("/api/users/1");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);


        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains(""" 
                        dateOfBirth":"2012-01-20:20-50-42","firstName":"Bob","gender":"male","id":1,"lastName":"Kåre","username":"Lulu""");

        // Set up connection for GET request and check second sampleUser.
        connection = openConnection("/api/users/2");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);


        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains(""" 
                        dateOfBirth":"2011-12-20:20-50-42","firstName":"exampleFirstName","gender":"male","id":2,"lastName":"exampleLastName","username":"exampleUser""");
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
                        dateOfBirth":"2012-01-20:20-50-42","firstName":"Bob","gender":"male","id":1,"lastName":"Kåre","username":"Lulu""");
    }

    @Test
    void shouldListAllUsers() throws IOException {
        // Create sample user, map it to JSON format and send post-url and json to doPostRequest
        var firstSampleUser = SampleData.createSampleUser(1);
        String firstSampleUserJson = mapper.writeValueAsString(firstSampleUser);
        doPostRequest("api/users", firstSampleUserJson);

        var secondSampleUser = SampleData.createSampleUser(2);
        String secondSampleUserJson = mapper.writeValueAsString(secondSampleUser);
        doPostRequest("api/users", secondSampleUserJson);

        var thirdSampleUser = SampleData.createSampleUser(3);
        String thirdSampleUserJson = mapper.writeValueAsString(thirdSampleUser);
        doPostRequest("api/users", thirdSampleUserJson);


        var connection = openConnection("/api/users/");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);


        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("""
                        dateOfBirth":"2012-01-20:20-50-42","firstName":"Bob","gender":"male","id":1,"lastName":"Kåre","username":"Lulu""")
                .contains("""
                        dateOfBirth":"2011-12-20:20-50-42","firstName":"exampleFirstName","gender":"male","id":2,"lastName":"exampleLastName","username":"exampleUser""")
                .contains("""
                        dateOfBirth":"2012-12-20:21-50-42","firstName":"exampleFirstName2","gender":"male","id":3,"lastName":"exampleLastName2","username":"exampleUser2""");
    }


    @Test
    void shouldEditUser() throws IOException {

        // CREATE USER
        var firstSampleUser = SampleData.createSampleUser(1);
        String firstSampleUserJson = mapper.writeValueAsString(firstSampleUser);
        doPostRequest("api/users", firstSampleUserJson);

        //EDIT USER
        firstSampleUser.setFirstName("EditFirstName");
        firstSampleUserJson = mapper.writeValueAsString(firstSampleUser);
        doPostRequest("api/users/1", firstSampleUserJson);

        var connection = openConnection("/api/users/");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);
        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("""
                        firstName":"EditFirstName""");

    }

    @Test
    void shouldListAllThreadsUserIsMemberOf() throws IOException {

        //USER
        User user = SampleData.createSampleUser(1);
        String userJson = mapper.writeValueAsString(user);
        doPostRequest("api/users", userJson);


        //SECOND USER
        User secondUser = SampleData.createSampleUser(2);
        String secondUserJson = mapper.writeValueAsString(secondUser);
        doPostRequest("api/users", secondUserJson);


        //SECOND USER
        User thirdUser = SampleData.createSampleUser(3);
        String thirdUserJson = mapper.writeValueAsString(thirdUser);
        doPostRequest("api/users", thirdUserJson);


        // Update user from database to get the id.
        var firstUserConnection = openConnection("/api/users/1");
        user = mapper.readValue(firstUserConnection.getInputStream(), User.class);

        var secondUserConnection = openConnection("/api/users/2");
        secondUser = mapper.readValue(secondUserConnection.getInputStream(), User.class);

        var thirdUserConnection = openConnection("/api/users/3");
        thirdUser = mapper.readValue(thirdUserConnection.getInputStream(), User.class);


        //MESSAGE
        Message message = SampleData.createSampleMessage(1);

        //THREAD

        var firstThreadDto = new MessageThreadDto();
        firstThreadDto.setCreator(user);
        firstThreadDto.setThreadTitle("Title 1");
        firstThreadDto.setMessage(message.getBody());
        firstThreadDto.setMembers(List.of(secondUser));
        String threadJson = mapper.writeValueAsString(firstThreadDto);
        doPostRequest("/api/thread", threadJson);

        var secondThreadDto = new MessageThreadDto();
        secondThreadDto.setCreator(thirdUser);
        secondThreadDto.setThreadTitle("Title 1");
        secondThreadDto.setMessage(message.getBody());
        secondThreadDto.setMembers(List.of(secondUser));
        String secondThreadJson = mapper.writeValueAsString(secondThreadDto);
        doPostRequest("/api/thread", secondThreadJson);


        var connection = openConnection("/api/users/2/threads");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);
        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("""
                        ,"id":1""")
                .contains("""
                        ,"id":2""");
    }
}
