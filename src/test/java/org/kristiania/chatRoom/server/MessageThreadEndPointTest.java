package org.kristiania.chatRoom.server;

import org.junit.jupiter.api.Test;
import org.kristiania.chatRoom.MessageThread;
import org.kristiania.chatRoom.User;
import org.kristiania.chatRoom.database.SampleData;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageThreadEndPointTest extends AbstractServerTest {


    @Test
    void shouldAddAndListThread() throws IOException {

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


        var threadPostConnection = openConnection("/api/thread/1");
        threadPostConnection.setRequestMethod("POST");
        threadPostConnection.setRequestProperty("Content-Type", "application/json");
        threadPostConnection.setDoOutput(true);
        threadPostConnection.getOutputStream().write(threadJson.getBytes(StandardCharsets.UTF_8));

        assertThat(threadPostConnection.getResponseCode())
                .as(threadPostConnection.getResponseMessage() + " for " + threadPostConnection.getURL())
                .isEqualTo(204);

        var connection = openConnection("/api/thread");
        assertThat(connection.getResponseCode())
                .as(connection.getResponseMessage() + " for " + connection.getURL())
                .isEqualTo(200);

        assertThat(connection.getInputStream()).asString(StandardCharsets.UTF_8)
                .contains("""
                        "creator":{"dateOfBirth":"2012-01-20","firstName":"Bob","gender":"male","id":1,"lastName":"Kåre","username":"Lulu"},"id":1""");
    }


}
