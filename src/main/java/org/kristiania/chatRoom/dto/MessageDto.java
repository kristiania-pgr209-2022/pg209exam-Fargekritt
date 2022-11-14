package org.kristiania.chatRoom.dto;

import org.kristiania.chatRoom.MessageThread;
import org.kristiania.chatRoom.User;

public class MessageDto {

    private User user;

    private MessageThread thread;

    private String body;
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MessageThread getThread() {
        return thread;
    }

    public void setThread(MessageThread thread) {
        this.thread = thread;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
