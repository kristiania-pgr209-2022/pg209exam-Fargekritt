package org.kristiania.chatRoom.server;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;


public class ChatRoomServerTest extends AbstractServerTest {



    @Test
    void shouldServeHomepage() throws Exception {
        var connection = openConnection("/");

        assertThat(connection.getResponseCode())
                .as(connection.getResponseCode() + " For " + connection.getURL())
                .isEqualTo(200);

        assertThat(connection.getInputStream())
                .asString(StandardCharsets.UTF_8)
                .contains("<title>Ultra Shop</title>");

    }



}
