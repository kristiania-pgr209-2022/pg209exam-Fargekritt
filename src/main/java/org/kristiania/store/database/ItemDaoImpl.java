package org.kristiania.store.database;

import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import org.kristiania.store.Item;

import java.util.List;

public class ItemDaoImpl implements ItemDao {

    private final EntityManager entityManager;

    @Inject
    public ItemDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void save(Item item)  {
        entityManager.persist(item);
    }


    @Override
    public Item retrieve(long id) {

        return entityManager.find(Item.class, id);
    }

    @Override
    public List<Item> listAll()  {

        return entityManager.createQuery(entityManager.getCriteriaBuilder().createQuery(Item.class)).getResultList();
    }
}
