package org.kristiania.chatRoom.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;

@Embeddable
public class ThreadMemberKey implements Serializable {

    @Column(name = "thead_id")
    long threadId;

    @Column(name = "user_id")
    long userId;
}
