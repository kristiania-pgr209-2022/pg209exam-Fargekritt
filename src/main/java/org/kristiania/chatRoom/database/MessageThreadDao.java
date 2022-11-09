package org.kristiania.chatRoom.database;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import org.kristiania.chatRoom.MessageThread;

import java.util.List;

public class MessageThreadDao {

    private final EntityManager entityManager;

    @Inject
    public MessageThreadDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public MessageThread retrieve(long id) {
        return entityManager.find(MessageThread.class, id);
    }


    public void save(MessageThread messageThread) {
        entityManager.persist(messageThread);
    }


    public List<MessageThread> listAll() {
        return entityManager.createQuery("SELECT t from MessageThread t").getResultList();
    }
}
