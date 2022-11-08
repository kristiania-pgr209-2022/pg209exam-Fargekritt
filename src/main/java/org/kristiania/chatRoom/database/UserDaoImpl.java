package org.kristiania.chatRoom.database;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;

import org.kristiania.chatRoom.User;

import java.util.List;

public class UserDaoImpl implements UserDao {


    private final EntityManager entityManager;

    @Inject
    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public void save(User user) {
        entityManager.persist(user);
    }


    @Override
    public User retrieve(long id) {

        return entityManager.find(User.class, id);
    }

    @Override
    public List<User> listAll() {
        return entityManager.createQuery("SELECT u from User u" ).getResultList();}
}
