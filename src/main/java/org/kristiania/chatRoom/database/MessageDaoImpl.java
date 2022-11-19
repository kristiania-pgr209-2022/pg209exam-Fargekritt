package org.kristiania.chatRoom.database;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.kristiania.chatRoom.entities.Message;

import java.util.List;

public class  MessageDaoImpl implements MessageDao {
    private final EntityManager entityManager;

    @Inject
    public MessageDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Message retrieve(int id) {
        return entityManager.find(Message.class, id);
    }

    @Override
    public List<Message> findByUser(long userId) {
        return entityManager.createQuery("SELECT m from Message m where m.user.id = :userid")
                .setParameter("userid", userId)
                .getResultList();
    }

    @Override
    public void save(Message message) {
        entityManager.persist(message);
    }

    @Override
    public List<Message> listAll() {
        return entityManager.createQuery(entityManager.getCriteriaBuilder().createQuery(Message.class)).getResultList();
    }

    @Override
    public List<Message> findByThreadId(long id) {
        return entityManager.createQuery("SELECT m from Message m where m.thread.id = :threadid")
                .setParameter("threadid", id)
                .getResultList();
    }
}
