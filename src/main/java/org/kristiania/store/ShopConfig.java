package org.kristiania.store;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.process.internal.RequestScoped;
import org.glassfish.jersey.server.ResourceConfig;
import org.kristiania.store.database.ItemDao;
import org.kristiania.store.database.ItemDaoImpl;

import javax.naming.NamingException;

public class ShopConfig extends ResourceConfig {
    private final EntityManagerFactory entityManagerFactory;
    private final ThreadLocal<EntityManager> requestEntityManager = new ThreadLocal<>();

    public ShopConfig(EntityManagerFactory entityManagerFactory) {
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
