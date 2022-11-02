package org.kristiania.chatRoom.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.eclipse.jetty.plus.jndi.Resource;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kristiania.chatRoom.Item;

import javax.naming.NamingException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class ItemDaoTest {


    private final ItemDao dao;

    private final EntityManager entityManager;


    public ItemDaoTest() throws NamingException {
        JdbcDataSource datasource = InMemoryDataSource.createTestDataSource();

        new Resource("jdbc/dataSource", datasource);
        this.entityManager = Persistence.createEntityManagerFactory("ChatRoom").createEntityManager();

        dao = new ItemDaoImpl(entityManager);
    }

    @BeforeEach
    void setUp() {
        entityManager.getTransaction().begin();
    }

    @AfterEach
    void tearDown() {
        entityManager.getTransaction().rollback();
    }

    @Test
    void shouldRetrieveSavedItem() throws SQLException {
        var item = sampleItem();
        dao.save(item);
        flush();
        assertThat(dao.retrieve(item.getId()))
                .usingRecursiveComparison()
                .isEqualTo(item)
                .isNotSameAs(item);
    }

    private void flush() {
        entityManager.flush();
        entityManager.clear();
    }

    @Test
    void shouldRetrieveNullForMissingItem() throws SQLException {
        assertThat(dao.retrieve(-1)).isNull();
    }

    @Test
    void shouldRetrieveAllItems() throws SQLException {
        var item1 = sampleItem();
        var item2 = sampleItem();
        var item3 = sampleItem();
        dao.save(item1);
        dao.save(item2);
        dao.save(item3);
        flush();

        assertThat(dao.listAll())
                .extracting(Item::getId)
                .contains(item1.getId(), item2.getId(), item3.getId());
    }

    private Item sampleItem() {

        var sampleItem = new Item();
        sampleItem.setName("SampleItem");
        sampleItem.setArtNumber("1234-1234");
        sampleItem.setCategory("SampleItemCategory");
        sampleItem.setDescription("SampleItemDescription");

        return sampleItem;
    }
}
