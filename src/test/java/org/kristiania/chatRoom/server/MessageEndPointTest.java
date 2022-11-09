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
        doPostRequest("api/users",userJson);


        //THREAD
        MessageThread thread = SampleData.createSampleThread();
        String threadJson = mapper.writeValueAsString(thread);
        doPostRequest("/api/thread/1",threadJson);

        //Message
        Message message = SampleData.createSampleMessage(1);
        String messageJson = mapper.writeValueAsString(message);
        doPostRequest("/api/messages/user/1/thread/1",messageJson);

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
        doPostRequest("api/users",userJson);


        //THREAD
        MessageThread thread = SampleData.createSampleThread();
        String threadJson = mapper.writeValueAsString(thread);
        doPostRequest("/api/thread/1",threadJson);

        //Message
        Message message = SampleData.createSampleMessage(1);
        String messageJson = mapper.writeValueAsString(message);
        doPostRequest("/api/messages/user/1/thread/1",messageJson);

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
        doPostRequest("api/users",userJson);


        //THREAD
        MessageThread thread = SampleData.createSampleThread();
        String threadJson = mapper.writeValueAsString(thread);
        doPostRequest("/api/thread/1",threadJson);

        //Message
        Message message = SampleData.createSampleMessage(1);
        String messageJson = mapper.writeValueAsString(message);
        doPostRequest("/api/messages/user/1/thread/1",messageJson);

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
