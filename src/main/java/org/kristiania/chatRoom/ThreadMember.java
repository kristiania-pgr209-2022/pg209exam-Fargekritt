package org.kristiania.chatRoom;

import jakarta.persistence.*;

@Entity
@Table(name = "thread_members")
public class ThreadMember {

    @EmbeddedId
    ThreadMemberKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @MapsId("threadId")
    @JoinColumn(name = "thread_id")
    MessageThread messageThread;

}
