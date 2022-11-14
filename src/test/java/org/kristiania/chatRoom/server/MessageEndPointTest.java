package org.kristiania.chatRoom.server;

import org.junit.jupiter.api.Test;
import org.kristiania.chatRoom.Message;
import org.kristiania.chatRoom.Dto.MessageThread;
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
        doPostRequest("api/users",userJson);

        //SECOND USER
        User secondUser = SampleData.createSampleUser(2);
        String secondUserJson = mapper.writeValueAsString(secondUser);
        doPostRequest("api/users",secondUserJson);

        //THREAD
        MessageThread thread = SampleData.createSampleThread();
        String threadJson = mapper.writeValueAsString(thread);
        doPostRequest("/api/thread/1",threadJson);

        //Message
        Message message = SampleData.createSampleMessage(1);
        String messageJson = mapper.writeValueAsString(message);
        doPostRequest("/api/messages/user/1/thread/1",messageJson);

        //SECOND Message
        Message secondMessage = SampleData.createSampleMessage(2);
        String secondMessageJson = mapper.writeValueAsString(secondMessage);
        doPostRequest("/api/messages/user/2/thread/1",secondMessageJson);

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

        //SECOND USER
        User secondUser = SampleData.createSampleUser(2);
        String secondUserJson = mapper.writeValueAsString(secondUser);
        doPostRequest("api/users",secondUserJson);

        //THREAD
        MessageThread thread = SampleData.createSampleThread();
        String threadJson = mapper.writeValueAsString(thread);
        doPostRequest("/api/thread/1",threadJson);

        //Message
        Message message = SampleData.createSampleMessage(1);
        String messageJson = mapper.writeValueAsString(message);
        doPostRequest("/api/messages/user/1/thread/1",messageJson);

        //SECOND Message
        Message secondMessage = SampleData.createSampleMessage(2);
        String secondMessageJson = mapper.writeValueAsString(secondMessage);
        doPostRequest("/api/messages/user/2/thread/1",secondMessageJson);

        //THIRD Message
        Message thirdMessage = SampleData.createSampleMessage(3);
        String thirdMessageJson = mapper.writeValueAsString(thirdMessage);
        doPostRequest("/api/messages/user/2/thread/1",thirdMessageJson);

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

        //SECOND USER
        User secondUser = SampleData.createSampleUser(2);
        String secondUserJson = mapper.writeValueAsString(secondUser);
        doPostRequest("api/users",secondUserJson);

        //THREAD
        MessageThread thread = SampleData.createSampleThread();
        String threadJson = mapper.writeValueAsString(thread);
        doPostRequest("/api/thread/1",threadJson);

        //FIRST MESSAGE
        Message message = SampleData.createSampleMessage(1);
        String messageJson = mapper.writeValueAsString(message);
        doPostRequest("/api/messages/user/1/thread/1",messageJson);

        //SECOND MESSAGE
        Message secondMessage = SampleData.createSampleMessage(2);
        String secondMessageJson = mapper.writeValueAsString(secondMessage);
        doPostRequest("/api/messages/user/2/thread/1",secondMessageJson);

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
