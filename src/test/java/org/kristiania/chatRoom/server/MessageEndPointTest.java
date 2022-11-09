package org.kristiania.chatRoom.server;

import org.junit.jupiter.api.Test;
import org.kristiania.chatRoom.Message;
import org.kristiania.chatRoom.MessageThread;
import org.kristiania.chatRoom.User;
import org.kristiania.chatRoom.database.SampleData;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import static org.assertj.core.api.Assertions.assertThat;

public class MessageEndPointTest extends AbstractServerTest{


    @Test
    void shouldAddAndListAllMessages() throws IOException {


        //USER
        User user = SampleData.createSampleUser(1);
        String userJson = mapper.writeValueAsString(user);

        var userPostConnection = getPostConnection("api/users");
        userPostConnection.getOutputStream().write(userJson.getBytes(StandardCharsets.UTF_8));

        assertThat(userPostConnection.getResponseCode())
                .as(userPostConnection.getResponseMessage() + " for " + userPostConnection.getURL())
                .isEqualTo(204);

        //THREAD
        MessageThread thread = SampleData.createSampleThread();
        String threadJson = mapper.writeValueAsString(thread);


        var threadPostConnection = getPostConnection("/api/thread/1");
        threadPostConnection.getOutputStream().write(threadJson.getBytes(StandardCharsets.UTF_8));

        assertThat(threadPostConnection.getResponseCode())
                .as(threadPostConnection.getResponseMessage() + " for " + threadPostConnection.getURL())
                .isEqualTo(204);

        //Message
        Message message = SampleData.createSampleMessage(1);
        String messageJson = mapper.writeValueAsString(message);

        var messagePostConnection = getPostConnection("/api/messages/user/1/thread/1");
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


    @Test
    void shouldListMessageByUserId() throws IOException {


        //USER
        User user = SampleData.createSampleUser(1);
        String userJson = mapper.writeValueAsString(user);

        var userPostConnection = getPostConnection("/api/users");
        userPostConnection.getOutputStream().write(userJson.getBytes(StandardCharsets.UTF_8));

        assertThat(userPostConnection.getResponseCode())
                .as(userPostConnection.getResponseMessage() + " for " + userPostConnection.getURL())
                .isEqualTo(204);


        //THREAD
        MessageThread thread = SampleData.createSampleThread();
        String threadJson = mapper.writeValueAsString(thread);


        var threadPostConnection = getPostConnection("/api/thread/1");
        threadPostConnection.getOutputStream().write(threadJson.getBytes(StandardCharsets.UTF_8));

        assertThat(threadPostConnection.getResponseCode())
                .as(threadPostConnection.getResponseMessage() + " for " + threadPostConnection.getURL())
                .isEqualTo(204);

        //Message
        Message message = SampleData.createSampleMessage(1);
        String messageJson = mapper.writeValueAsString(message);

        var messagePostConnection = getPostConnection("/api/messages/user/1/thread/1");
        messagePostConnection.getOutputStream().write(messageJson.getBytes(StandardCharsets.UTF_8));


        assertThat(messagePostConnection.getResponseCode())
                .as(messagePostConnection.getResponseMessage() + " for " + messagePostConnection.getURL())
                .isEqualTo(204);

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

    }


    @Test
    void shouldGetMessageByMessageId() throws IOException {

        //USER
        User user = SampleData.createSampleUser(1);
        String userJson = mapper.writeValueAsString(user);

        var userPostConnection = getPostConnection("/api/users");
        userPostConnection.getOutputStream().write(userJson.getBytes(StandardCharsets.UTF_8));

        assertThat(userPostConnection.getResponseCode())
                .as(userPostConnection.getResponseMessage() + " for " + userPostConnection.getURL())
                .isEqualTo(204);


        //THREAD
        MessageThread thread = SampleData.createSampleThread();
        String threadJson = mapper.writeValueAsString(thread);


        var threadPostConnection = getPostConnection("/api/thread/1");
        threadPostConnection.getOutputStream().write(threadJson.getBytes(StandardCharsets.UTF_8));

        assertThat(threadPostConnection.getResponseCode())
                .as(threadPostConnection.getResponseMessage() + " for " + threadPostConnection.getURL())
                .isEqualTo(204);

        //Message
        Message message = SampleData.createSampleMessage(1);
        String messageJson = mapper.writeValueAsString(message);

        var messagePostConnection = getPostConnection("/api/messages/user/1/thread/1");
        messagePostConnection.getOutputStream().write(messageJson.getBytes(StandardCharsets.UTF_8));


        assertThat(messagePostConnection.getResponseCode())
                .as(messagePostConnection.getResponseMessage() + " for " + messagePostConnection.getURL())
                .isEqualTo(204);

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

    }

}
