package org.kristiania.chatRoom.database;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.kristiania.chatRoom.Message;
import org.kristiania.chatRoom.MessageThread;
import org.kristiania.chatRoom.ThreadMember;

import java.util.List;

public class ThreadMemberDao {

    private final EntityManager entityManager;

    @Inject
    public ThreadMemberDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    public ThreadMember retrieve(long id) {
        return entityManager.find(ThreadMember.class, id);
    }

    public List<MessageThread> findByUser(long userId) {
        return entityManager.createQuery("SELECT tm.messageThread from ThreadMember tm where tm.user.id = :userid")
                .setParameter("userid", userId)
                .getResultList();
    }


    public void save(ThreadMember messageThread) {
        entityManager.persist(messageThread);
    }

}
