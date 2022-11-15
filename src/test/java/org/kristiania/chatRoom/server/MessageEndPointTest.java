package org.kristiania.chatRoom.server;

import org.junit.jupiter.api.Test;
import org.kristiania.chatRoom.Message;
import org.kristiania.chatRoom.MessageThread;
import org.kristiania.chatRoom.User;
import org.kristiania.chatRoom.database.MessageThreadDao;
import org.kristiania.chatRoom.database.SampleData;
import org.kristiania.chatRoom.dto.MessageDto;
import org.kristiania.chatRoom.dto.MessageThreadDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import static org.assertj.core.api.Assertions.assertThat;

public class MessageEndPointTest extends AbstractServerTest{

    @Test
    void shouldAddAndListAllMessages() throws IOException {


        //USER
        User user = SampleData.createSampleUser(1);
        String userJson = mapper.writeValueAsString(user);
        doPostRequest("api/users",userJson);

        var user1Connection = openConnection("/api/users/1");
        user = mapper.readValue(user1Connection.getInputStream(),User.class);

        //SECOND USER
        User secondUser = SampleData.createSampleUser(2);
        String secondUserJson = mapper.writeValueAsString(secondUser);
        doPostRequest("api/users",secondUserJson);

        var user2Connection = openConnection("/api/users/2");
        secondUser = mapper.readValue(user2Connection.getInputStream(),User.class);

        //MESSAGE
        Message message = SampleData.createSampleMessage(1);

        //THREAD
        var threadDto = new MessageThreadDto();
        threadDto.setCreator(user);
        threadDto.setTitle("Title 1");
        threadDto.setMessage(message.getBody());
        threadDto.setReceiverId(2);
        String threadJson = mapper.writeValueAsString(threadDto);
        doPostRequest("/api/thread",threadJson);

        var threadConnection = openConnection("/api/thread/1");
        var thread = mapper.readValue(threadConnection.getInputStream(), MessageThread.class);

        //SECOND Message
        Message secondMessage = SampleData.createSampleMessage(2);
        var message2 = new MessageDto();
        message2.setUser(secondUser);
        message2.setBody(secondMessage.getBody());
        message2.setThread(thread);

        String secondMessageJson = mapper.writeValueAsString(message2);
        doPostRequest("/api/messages",secondMessageJson);

        var connection = openConnection("/api/messages/");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);


        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("""
                        body":"This is a testing body for a message""")
                .contains("""
                        firstName":"Bob""")
                .contains("""
                        body":"This is another testing body for a message"""
                        )
                .contains("""
                        firstName":"exampleFirstName""");
    }

    @Test
    void shouldListMessageByUserId() throws IOException {

        //USER
        User user = SampleData.createSampleUser(1);
        String userJson = mapper.writeValueAsString(user);
        doPostRequest("api/users",userJson);

        var user1Connection = openConnection("/api/users/1");
        user = mapper.readValue(user1Connection.getInputStream(),User.class);

        //SECOND USER
        User secondUser = SampleData.createSampleUser(2);
        String secondUserJson = mapper.writeValueAsString(secondUser);
        doPostRequest("api/users",secondUserJson);

        var user2Connection = openConnection("/api/users/2");
        secondUser = mapper.readValue(user2Connection.getInputStream(),User.class);

        //Message
        Message message = SampleData.createSampleMessage(1);

        //THREAD
        var threadDto = new MessageThreadDto();
        threadDto.setCreator(user);
        threadDto.setTitle("Title 1");
        threadDto.setMessage(message.getBody());
        threadDto.setReceiverId(2);
        String threadJson = mapper.writeValueAsString(threadDto);
        doPostRequest("/api/thread",threadJson);

        var threadConnection = openConnection("/api/thread/1");
        var thread = mapper.readValue(threadConnection.getInputStream(), MessageThread.class);



        //SECOND Message
        Message secondMessage = SampleData.createSampleMessage(2);
        var message2 = new MessageDto();
        message2.setUser(secondUser);
        message2.setBody(secondMessage.getBody());
        message2.setThread(thread);

        String secondMessageJson = mapper.writeValueAsString(message2);
        doPostRequest("/api/messages",secondMessageJson);

        //THIRD Message
        Message thirdMessage = SampleData.createSampleMessage(3);
        var message3 = new MessageDto();
        message3.setUser(secondUser);
        message3.setBody(thirdMessage.getBody());
        message3.setThread(thread);

        String thirdMessageJson = mapper.writeValueAsString(message3);
        doPostRequest("/api/messages",thirdMessageJson);

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
                        firstName":"Bob""");

        // Messages made my second user.
         connection = openConnection("/api/messages/user/2");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);

        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("""
                        body":"This is another testing body for a message""")
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
        doPostRequest("api/users",userJson);

        var user1Connection = openConnection("/api/users/1");
        user = mapper.readValue(user1Connection.getInputStream(),User.class);

        //SECOND USER
        User secondUser = SampleData.createSampleUser(2);
        String secondUserJson = mapper.writeValueAsString(secondUser);
        doPostRequest("api/users",secondUserJson);

        var user2Connection = openConnection("/api/users/2");
        secondUser = mapper.readValue(user2Connection.getInputStream(),User.class);


        //Message
        Message message = SampleData.createSampleMessage(1);


        //THREAD
        var threadDto = new MessageThreadDto();
        threadDto.setCreator(user);
        threadDto.setTitle("Title 1");
        threadDto.setMessage(message.getBody());
        threadDto.setReceiverId(2);
        String threadJson = mapper.writeValueAsString(threadDto);
        doPostRequest("/api/thread",threadJson);

        var threadConnection = openConnection("/api/thread/1");
        var thread = mapper.readValue(threadConnection.getInputStream(), MessageThread.class);


        //SECOND Message
        Message secondMessage = SampleData.createSampleMessage(2);
        var message2 = new MessageDto();
        message2.setUser(secondUser);
        message2.setBody(secondMessage.getBody());
        message2.setThread(thread);

        String secondMessageJson = mapper.writeValueAsString(message2);
        doPostRequest("/api/messages",secondMessageJson);

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
                        firstName":"exampleFirstName""");
    }
}
