package org.kristiania.chatRoom;

import jakarta.persistence.*;
import org.kristiania.chatRoom.Dto.MessageThread;

@Entity
@Table(name = "thread_members")
public class ThreadMember {

    @EmbeddedId
    ThreadMemberKey id;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    public MessageThread getMessageThread() {
        return messageThread;
    }

    public void setMessageThread(MessageThread messageThread) {
        this.messageThread = messageThread;
    }

    @ManyToOne
    @MapsId("threadId")
    @JoinColumn(name = "thread_id")
    MessageThread messageThread;

}
