package org.kristiania.chatRoom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;
import org.kristiania.chatRoom.database.ItemDao;
import org.kristiania.chatRoom.database.ItemDaoImpl;

public class ChatRoomConfig extends ResourceConfig {
    private final EntityManagerFactory entityManagerFactory;
    private final ThreadLocal<EntityManager> requestEntityManager = new ThreadLocal<>();

    public ChatRoomConfig(EntityManagerFactory entityManagerFactory) {
        super(ItemEndPoint.class);
        this.entityManagerFactory = entityManagerFactory;
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(ItemDaoImpl.class).to(ItemDao.class);
                bindFactory(requestEntityManager::get)
                        .to(EntityManager.class)
                        .in(RequestScoped.class);
            }
        });
    }

    public EntityManager createEntityMangerForRequest() {
        requestEntityManager.set(entityManagerFactory.createEntityManager());
        return requestEntityManager.get();
    }

    public void cleanRequestEntityManager() {
        requestEntityManager.remove();
    }
}
