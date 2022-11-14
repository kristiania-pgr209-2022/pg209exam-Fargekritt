package org.kristiania.chatRoom;

public class MessageDto {

    private User user;

    private MessageThread thread;
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


}
