package org.kristiania.chatRoom.database;

import jakarta.persistence.EntityManager;
import org.kristiania.chatRoom.Message;
import org.kristiania.chatRoom.User;

import java.util.ArrayList;
import java.util.List;

public class MessageDao {
    private final EntityManager entityManager;

    public MessageDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Message retrieve(int id){
        return entityManager.find(Message.class, id);
    }

    public List<Message> findByUser(long userId) {
        return new ArrayList<>(entityManager.find(User.class, userId).getMessages());
    }
}
