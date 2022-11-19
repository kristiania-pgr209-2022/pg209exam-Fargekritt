package org.kristiania.chatRoom.database;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import org.kristiania.chatRoom.entities.MessageThread;

import java.util.List;

public class MessageThreadDaoImpl implements MessageThreadDao {

    private final EntityManager entityManager;

    @Inject
    public MessageThreadDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public MessageThread retrieve(long id) {
        return entityManager.find(MessageThread.class, id);
    }


    @Override
    public void save(MessageThread messageThread) {
        entityManager.persist(messageThread);
    }


    @Override
    public List<MessageThread> listAll() {
        return entityManager.createQuery("SELECT t from MessageThread t").getResultList();
    }

    @Override
    public List<MessageThread> listAllByUserId(long id){
        return entityManager.createQuery("SELECT t from MessageThread t WHERE t.creator.id = :id")
                .setParameter("id", id)
                .getResultList();
    }
}
