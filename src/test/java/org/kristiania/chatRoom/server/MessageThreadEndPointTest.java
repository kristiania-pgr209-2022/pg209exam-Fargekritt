package org.kristiania.chatRoom.server;

import org.junit.jupiter.api.Test;
import org.kristiania.chatRoom.dto.MemberDto;
import org.kristiania.chatRoom.entities.Message;
import org.kristiania.chatRoom.entities.MessageThread;
import org.kristiania.chatRoom.entities.User;
import org.kristiania.chatRoom.database.SampleData;
import org.kristiania.chatRoom.dto.MessageThreadDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageThreadEndPointTest extends AbstractServerTest {


    @Test
    void shouldAddAndListThread() throws IOException {

        //USER
        User firstUser = SampleData.createSampleUser(1);
        String userJson = mapper.writeValueAsString(firstUser);
        doPostRequest("api/users",userJson);


        //SECOND USER
        User secondUser = SampleData.createSampleUser(2);
        String secondUserJson = mapper.writeValueAsString(secondUser);
        doPostRequest("api/users",secondUserJson);

        // Update users from database to get id.
        var firstUserConnection = openConnection("/api/users/1");
        firstUser = mapper.readValue(firstUserConnection.getInputStream(),User.class);

        var secondUserConnection = openConnection("/api/users/2");
        secondUser = mapper.readValue(secondUserConnection.getInputStream(),User.class);


        //MESSAGE
        Message message = SampleData.createSampleMessage(1);

        //THREAD
        var threadDto = new MessageThreadDto();
        threadDto.setCreator(firstUser);
        threadDto.setThreadTitle("Title 1");
        threadDto.setMessage(message.getBody());
        threadDto.setMembers(List.of(secondUser));
        String threadJson = mapper.writeValueAsString(threadDto);
        doPostRequest("/api/thread",threadJson);


        var connection = openConnection("/api/thread");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);

        assertThat(connection.getInputStream()).asString(StandardCharsets.UTF_8)
                .contains("""
                        "creator":{"dateOfBirth":"2012-01-20:20-50-42","firstName":"Bob","gender":"male","id":1,"lastName":"Kåre","username":"Lulu"},"id":1""");
    }


    @Test
    void shouldAddMemberToThread() throws IOException {

        //USER
        User user = SampleData.createSampleUser(1);
        String userJson = mapper.writeValueAsString(user);
        doPostRequest("api/users",userJson);


        //SECOND USER
        User secondUser = SampleData.createSampleUser(2);
        String secondUserJson = mapper.writeValueAsString(secondUser);
        doPostRequest("api/users",secondUserJson);


        //SECOND USER
        User thirdUser = SampleData.createSampleUser(3);
        String thirdUserJson = mapper.writeValueAsString(thirdUser);
        doPostRequest("api/users",thirdUserJson);


        // Update user from database to get the id.
        var firstUserConnection = openConnection("/api/users/1");
        user = mapper.readValue(firstUserConnection.getInputStream(),User.class);

        var secondUserConnection = openConnection("/api/users/2");
        secondUser = mapper.readValue(secondUserConnection.getInputStream(),User.class);

        var thirdUserConnection = openConnection("/api/users/3");
        thirdUser = mapper.readValue(thirdUserConnection.getInputStream(),User.class);


        //MESSAGE
        Message message = SampleData.createSampleMessage(1);

        //THREAD

        var threadDto = new MessageThreadDto();
        threadDto.setCreator(user);
        threadDto.setThreadTitle("Title 1");
        threadDto.setMessage(message.getBody());
        threadDto.setMembers(List.of(secondUser));
        String threadJson = mapper.writeValueAsString(threadDto);
        doPostRequest("/api/thread",threadJson);

        var threadConnection = openConnection("/api/thread/1");
        var thread = mapper.readValue(threadConnection.getInputStream(), MessageThread.class);

        //ADD a user to the thread
        MemberDto thirdMember = new MemberDto();
        thirdMember.setThread(thread);
        thirdMember.setUser(thirdUser);
        String thirdMemberJson = mapper.writeValueAsString(thirdMember);
        doPostRequest("/api/thread/member",thirdMemberJson);


        var connection = openConnection("/api/thread/1/members");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);

        assertThat(connection.getInputStream()).asString(StandardCharsets.UTF_8)
                .contains("""
                        {"dateOfBirth":"2012-01-20:20-50-42","firstName":"Bob","gender":"male","id":1,"lastName":"Kåre","username":"Lulu"}""")
                .contains("""
                        {"dateOfBirth":"2011-12-20:20-50-42","firstName":"exampleFirstName","gender":"male","id":2,"lastName":"exampleLastName","username":"exampleUser"}""")
                .contains("""
                        {"dateOfBirth":"2012-12-20:21-50-42","firstName":"exampleFirstName2","gender":"male","id":3,"lastName":"exampleLastName2","username":"exampleUser2"}""");
    }

}
