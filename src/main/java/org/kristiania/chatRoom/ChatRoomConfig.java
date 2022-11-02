package org.kristiania.chatRoom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;
import org.kristiania.chatRoom.database.ItemDao;
import org.kristiania.chatRoom.database.ItemDaoImpl;
import org.kristiania.chatRoom.database.UserDao;
import org.kristiania.chatRoom.database.UserDaoImpl;
import org.kristiania.chatRoom.endPoints.ItemEndPoint;
import org.kristiania.chatRoom.endPoints.UserEndPoint;

import java.util.HashMap;
import java.util.Map;

public class ChatRoomConfig extends ResourceConfig {
    private final EntityManagerFactory entityManagerFactory;
    private final ThreadLocal<EntityManager> requestEntityManager = new ThreadLocal<>();

    public ChatRoomConfig(EntityManagerFactory entityManagerFactory) {
        super(ItemEndPoint.class, UserEndPoint.class);
        this.entityManagerFactory = entityManagerFactory;
        register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(ItemDaoImpl.class).to(ItemDao.class);
                bind(UserDaoImpl.class).to(UserDao.class);
                bindFactory(requestEntityManager::get)
                        .to(EntityManager.class)
                        .in(RequestScoped.class);
            }
        });

        // To remove WADL warnings.
        Map<String, String> props = new HashMap<>();
        props.put("jersey.config.server.wadl.disableWadl", "true");
        setProperties(props);
    }

    public EntityManager createEntityMangerForRequest() {
        requestEntityManager.set(entityManagerFactory.createEntityManager());
        return requestEntityManager.get();
    }

    public void cleanRequestEntityManager() {
        requestEntityManager.remove();
    }
}
