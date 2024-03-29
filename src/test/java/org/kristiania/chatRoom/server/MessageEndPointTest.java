package org.kristiania.chatRoom.server;

import org.junit.jupiter.api.Test;
import org.kristiania.chatRoom.entities.Message;
import org.kristiania.chatRoom.entities.MessageThread;
import org.kristiania.chatRoom.entities.User;
import org.kristiania.chatRoom.database.SampleData;
import org.kristiania.chatRoom.dto.MessageDto;
import org.kristiania.chatRoom.dto.MessageThreadDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageEndPointTest extends AbstractServerTest {

    @Test
    void shouldAddAndListAllMessages() throws IOException {


        //USER
        User user = SampleData.createSampleUser(1);
        String userJson = mapper.writeValueAsString(user);
        doPostRequest("api/users", userJson);

        var firstUserConnection = openConnection("/api/users/1");
        user = mapper.readValue(firstUserConnection.getInputStream(), User.class);

        //SECOND USER
        User secondUser = SampleData.createSampleUser(2);
        String secondUserJson = mapper.writeValueAsString(secondUser);
        doPostRequest("api/users", secondUserJson);

        var secondUserConnection = openConnection("/api/users/2");
        secondUser = mapper.readValue(secondUserConnection.getInputStream(), User.class);

        //MESSAGE
        Message message = SampleData.createSampleMessage(1);

        //THREAD
        var threadDto = new MessageThreadDto();
        threadDto.setCreator(user);
        threadDto.setThreadTitle("Title 1");
        threadDto.setMessage(message.getBody());
        threadDto.setMessageTitle(message.getTitle());
        threadDto.setMembers(List.of(secondUser));
        String threadJson = mapper.writeValueAsString(threadDto);
        doPostRequest("/api/thread", threadJson);

        var threadConnection = openConnection("/api/thread/1");
        var thread = mapper.readValue(threadConnection.getInputStream(), MessageThread.class);

        //SECOND Message
        Message secondMessage = SampleData.createSampleMessage(2);
        var secondMessageDto = new MessageDto();
        secondMessageDto.setUser(secondUser);
        secondMessageDto.setTitle(secondMessage.getTitle());
        secondMessageDto.setBody(secondMessage.getBody());
        secondMessageDto.setThread(thread);

        String secondMessageJson = mapper.writeValueAsString(secondMessageDto);
        doPostRequest("/api/messages", secondMessageJson);

        var connection = openConnection("/api/messages/");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);


        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("""
                        body":"This is a testing body for a message""")
                .contains("""
                        title":"SampleOneTestTitle""")
                .contains("""
                        firstName":"Bob""")
                .contains("""
                        body":"This is another testing body for a message"""
                )
                .contains("""
                        title":"SampleTwoTestTitle""")
                .contains("""
                        firstName":"exampleFirstName""");
    }

    @Test
    void shouldListMessageByUserId() throws IOException {

        //USER
        User user = SampleData.createSampleUser(1);
        String userJson = mapper.writeValueAsString(user);
        doPostRequest("api/users", userJson);

        var firstUserConnection = openConnection("/api/users/1");
        user = mapper.readValue(firstUserConnection.getInputStream(), User.class);

        //SECOND USER
        User secondUser = SampleData.createSampleUser(2);
        String secondUserJson = mapper.writeValueAsString(secondUser);
        doPostRequest("api/users", secondUserJson);

        var secondUserConnection = openConnection("/api/users/2");
        secondUser = mapper.readValue(secondUserConnection.getInputStream(), User.class);

        //Message
        Message message = SampleData.createSampleMessage(1);

        //THREAD
        var threadDto = new MessageThreadDto();
        threadDto.setCreator(user);
        threadDto.setThreadTitle("Title 1");
        threadDto.setMessageTitle(message.getTitle());
        threadDto.setMessage(message.getBody());
        threadDto.setMembers(List.of(secondUser));

        String threadJson = mapper.writeValueAsString(threadDto);
        doPostRequest("/api/thread", threadJson);

        var threadConnection = openConnection("/api/thread/1");
        var thread = mapper.readValue(threadConnection.getInputStream(), MessageThread.class);


        //SECOND Message
        Message secondMessage = SampleData.createSampleMessage(2);
        var secondMessageDto = new MessageDto();
        secondMessageDto.setUser(secondUser);
        secondMessageDto.setTitle(secondMessage.getTitle());
        secondMessageDto.setBody(secondMessage.getBody());
        secondMessageDto.setThread(thread);

        String secondMessageJson = mapper.writeValueAsString(secondMessageDto);
        doPostRequest("/api/messages", secondMessageJson);

        //THIRD Message
        Message thirdMessage = SampleData.createSampleMessage(3);
        var thirdMessageDto = new MessageDto();
        thirdMessageDto.setUser(secondUser);
        thirdMessageDto.setTitle(thirdMessage.getTitle());
        thirdMessageDto.setBody(thirdMessage.getBody());
        thirdMessageDto.setThread(thread);

        String thirdMessageJson = mapper.writeValueAsString(thirdMessageDto);
        doPostRequest("/api/messages", thirdMessageJson);

        // Message made by first user.
        var connection = openConnection("/api/messages/user/1");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);

        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("""
                        body":"This is a testing body for a message""")
                .contains("""
                        title":"SampleOneTestTitle""")
                .contains("""
                        firstName":"Bob""");

        // Messages made my second user.
        connection = openConnection("/api/messages/user/2");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);

        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("""
                        title":"SampleTwoTestTitle""")
                .contains("""
                        body":"This is another testing body for a message""")
                .contains("""
                        title":"SampleThreeTestTitle""")
                .contains("""
                        body":"This is the third testing body for a message""")
                .contains("""
                        firstName":"exampleFirstName""");


    }

    @Test
    void shouldGetMessageByMessageId() throws IOException {

        //USER
        User user = SampleData.createSampleUser(1);
        String userJson = mapper.writeValueAsString(user);
        doPostRequest("api/users", userJson);

        var firstUserConnection = openConnection("/api/users/1");
        user = mapper.readValue(firstUserConnection.getInputStream(), User.class);

        //SECOND USER
        User secondUser = SampleData.createSampleUser(2);
        String secondUserJson = mapper.writeValueAsString(secondUser);
        doPostRequest("api/users", secondUserJson);

        var secondUserConnection = openConnection("/api/users/2");
        secondUser = mapper.readValue(secondUserConnection.getInputStream(), User.class);


        //Message
        Message message = SampleData.createSampleMessage(1);


        //THREAD
        var threadDto = new MessageThreadDto();
        threadDto.setCreator(user);
        threadDto.setThreadTitle("Title 1");
        threadDto.setMessageTitle(message.getTitle());
        threadDto.setMessage(message.getBody());
        threadDto.setMembers(List.of(secondUser));
        String threadJson = mapper.writeValueAsString(threadDto);
        doPostRequest("/api/thread", threadJson);

        var threadConnection = openConnection("/api/thread/1");
        var thread = mapper.readValue(threadConnection.getInputStream(), MessageThread.class);


        //SECOND Message
        Message secondMessage = SampleData.createSampleMessage(2);
        var secondMessageDto = new MessageDto();
        secondMessageDto.setUser(secondUser);
        secondMessageDto.setTitle(secondMessage.getTitle());
        secondMessageDto.setBody(secondMessage.getBody());
        secondMessageDto.setThread(thread);

        String secondMessageJson = mapper.writeValueAsString(secondMessageDto);
        doPostRequest("/api/messages", secondMessageJson);

        // First user message.
        var connection = openConnection("/api/messages/1");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);

        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("""
                        body":"This is a testing body for a message""")
                .contains("""
                        title":"SampleOneTestTitle""")
                .contains("""
                        firstName":"Bob""");

        // Second user message.
        connection = openConnection("/api/messages/2");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);

        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("""
                        body":"This is another testing body for a message""")
                .contains("""
                        title":"SampleTwoTestTitle""")
                .contains("""
                        firstName":"exampleFirstName""");
    }

    @Test
    void shouldGetMessageByThreadId() throws IOException {

        //USER
        User firstUser = SampleData.createSampleUser(1);
        String userJson = mapper.writeValueAsString(firstUser);
        doPostRequest("api/users", userJson);

        var firstUserConnection = openConnection("/api/users/1");
        firstUser = mapper.readValue(firstUserConnection.getInputStream(), User.class);

        //SECOND USER
        User secondUser = SampleData.createSampleUser(2);
        String secondUserJson = mapper.writeValueAsString(secondUser);
        doPostRequest("api/users", secondUserJson);

        var secondUserConnection = openConnection("/api/users/2");
        secondUser = mapper.readValue(secondUserConnection.getInputStream(), User.class);

        //THIRD USER
        User thirdUser = SampleData.createSampleUser(3);
        String thirdUserJson = mapper.writeValueAsString(thirdUser);
        doPostRequest("api/users", thirdUserJson);

        var thirdUserConnection = openConnection("/api/users/3");
        thirdUser = mapper.readValue(thirdUserConnection.getInputStream(), User.class);

        //Message
        Message message = SampleData.createSampleMessage(1);

        //SECOND Message
        Message secondMessage = SampleData.createSampleMessage(2);


        //THREAD
        var threadDto = new MessageThreadDto();
        threadDto.setCreator(firstUser);
        threadDto.setThreadTitle("Title 1");
        threadDto.setMessageTitle(message.getTitle());
        threadDto.setMessage(message.getBody());
        threadDto.setMembers(List.of(secondUser));

        String threadJson = mapper.writeValueAsString(threadDto);
        doPostRequest("/api/thread", threadJson);


        //SECOND Thread

        var secondThreadDto = new MessageThreadDto();
        secondThreadDto.setCreator(secondUser);
        secondThreadDto.setThreadTitle("Title 2");
        secondThreadDto.setMessageTitle(secondMessage.getTitle());
        secondThreadDto.setMessage(secondMessage.getBody());
        secondThreadDto.setMembers(List.of(thirdUser));

        String secondThreadJson = mapper.writeValueAsString(secondThreadDto);
        doPostRequest("/api/thread", secondThreadJson);

        var secondThreadConnection = openConnection("/api/thread/2");
        var secondThread = mapper.readValue(secondThreadConnection.getInputStream(), MessageThread.class);

        //THIRD Message
        Message thirdMessage = SampleData.createSampleMessage(3);
        var thirdMessageDto = new MessageDto();
        thirdMessageDto.setUser(secondUser);
        thirdMessageDto.setTitle(thirdMessage.getTitle());
        thirdMessageDto.setBody(thirdMessage.getBody());
        thirdMessageDto.setThread(secondThread);

        String thirdMessageJson = mapper.writeValueAsString(thirdMessageDto);
        doPostRequest("/api/messages", thirdMessageJson);




        // First Thread messages.
        var connection = openConnection("/api/messages/thread/1");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);

        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("""
                        body":"This is a testing body for a message""")
                .contains("""
                        title":"SampleOneTestTitle""")
                .contains("""
                        firstName":"Bob""");

        // Second thread messages.
        connection = openConnection("/api/messages/thread/2");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);

        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("""
                        body":"This is another testing body for a message""")
                .contains("""
                        title":"SampleTwoTestTitle""")
                .contains("""
                        firstName":"exampleFirstName""");

        connection = openConnection("/api/messages/thread/2");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);

        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("""
                        body":"This is the third testing body for a message""")
                .contains("""
                        title":"SampleThreeTestTitle""");

    }
}
