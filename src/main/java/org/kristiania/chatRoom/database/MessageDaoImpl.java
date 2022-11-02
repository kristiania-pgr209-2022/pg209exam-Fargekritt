package org.kristiania.chatRoom.database;

import jakarta.persistence.EntityManager;
import org.kristiania.chatRoom.Message;
import org.kristiania.chatRoom.User;

import java.util.ArrayList;
import java.util.List;

public class MessageDaoImpl implements MessageDao {
    private final EntityManager entityManager;

    public MessageDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Message retrieve(int id) {
        return entityManager.find(Message.class, id);
    }

    @Override
    public List<Message> findByUser(long userId) {
        return new ArrayList<>(entityManager.find(User.class, userId).getMessages());
    }

    @Override
    public void save(Message message) {
        entityManager.persist(message);
    }

    @Override
    public List<Message> listAll() {
        return entityManager.createQuery(entityManager.getCriteriaBuilder().createQuery(Message.class)).getResultList();
    }
}
