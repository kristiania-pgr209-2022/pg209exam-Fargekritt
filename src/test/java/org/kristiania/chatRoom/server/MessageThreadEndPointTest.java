package org.kristiania.chatRoom.server;

import org.junit.jupiter.api.Test;
import org.kristiania.chatRoom.Message;
import org.kristiania.chatRoom.MessageThread;
import org.kristiania.chatRoom.User;
import org.kristiania.chatRoom.database.SampleData;
import org.kristiania.chatRoom.dto.MessageThreadDto;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageThreadEndPointTest extends AbstractServerTest {


    @Test
    void shouldAddAndListThread() throws IOException {

        //USER
        User user = SampleData.createSampleUser(1);
        String userJson = mapper.writeValueAsString(user);
        doPostRequest("api/users",userJson);


        //SECOND USER
        User secondUser = SampleData.createSampleUser(2);
        String secondUserJson = mapper.writeValueAsString(secondUser);
        doPostRequest("api/users",secondUserJson);

        var user2Connection = openConnection("/api/users/2");
        secondUser = mapper.readValue(user2Connection.getInputStream(),User.class);

        var user1Connection = openConnection("/api/users/1");
        user = mapper.readValue(user1Connection.getInputStream(),User.class);

        var userPostConnection = getPostConnection("api/users");
        userPostConnection.getOutputStream().write(userJson.getBytes(StandardCharsets.UTF_8));

        assertThat(userPostConnection.getResponseCode())
                .as(userPostConnection.getResponseMessage() + " for " + userPostConnection.getURL())
                .isEqualTo(204);


        //MESSAGE
        Message message = SampleData.createSampleMessage(1);

        //THREAD
        var threadDto = new MessageThreadDto();
        threadDto.setCreator(user);
        threadDto.setThreadTitle("Title 1");
        threadDto.setMessage(message.getBody());
        threadDto.setUser(secondUser);
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


}
