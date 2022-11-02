package org.kristiania.chatRoom.database;

import org.kristiania.chatRoom.Message;

import java.util.List;

public interface MessageDao {
    Message retrieve(int id);

    List<Message> findByUser(long userId);

    void save(Message message);

    List<Message> listAll();
}
