package org.kristiania.chatRoom.database;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.kristiania.chatRoom.entities.MessageThread;
import org.kristiania.chatRoom.entities.ThreadMember;
import org.kristiania.chatRoom.entities.User;

import java.util.List;

public class ThreadMemberDaoImpl implements ThreadMemberDao {

    private final EntityManager entityManager;

    @Inject
    public ThreadMemberDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }




    @Override
    public List<MessageThread> findByUser(long userId) {
        return entityManager.createQuery("SELECT tm.messageThread from ThreadMember tm where tm.user.id = :userid")
                .setParameter("userid", userId)
                .getResultList();
    }


    @Override
    public void save(ThreadMember messageThread) {
        entityManager.persist(messageThread);
    }

    @Override
    public List<User> findByThread(long id) {
        return entityManager.createQuery("SELECT tm.user from ThreadMember tm where tm.messageThread.id = :threadid")
                .setParameter("threadid", id)
                .getResultList();
    }
}
