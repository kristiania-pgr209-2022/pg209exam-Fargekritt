package org.kristiania.chatRoom.database;

import org.kristiania.chatRoom.entities.MessageThread;

import java.util.List;

public interface MessageThreadDao {
    MessageThread retrieve(long id);

    void save(MessageThread messageThread);

    List<MessageThread> listAll();

    List<MessageThread> listAllByUserId(long id);
}
