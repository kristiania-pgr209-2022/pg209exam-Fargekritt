package org.kristiania.chatRoom.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.eclipse.jetty.plus.jndi.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.NamingException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageThreadDaoTest {

    private final MessageThreadDao dao;
    private final UserDaoImpl userDao;

    private final EntityManager entityManager;



    public MessageThreadDaoTest() throws NamingException {
        var dataSource = InMemoryDataSource.createTestDataSource();
        new Resource("jdbc/dataSource", dataSource);
        this.entityManager = Persistence.createEntityManagerFactory("ChatRoom").createEntityManager();

        dao = new MessageThreadDao(entityManager);
        userDao = new UserDaoImpl(entityManager);
    }

    @BeforeEach
    void setUp() {
        entityManager.getTransaction().begin();
    }

    @AfterEach
    void tearDown() throws SQLException {
        entityManager.getTransaction().rollback();
        InMemoryDataSource.clearTestDataSource();
    }

    @Test
    void shouldSaveAndRetrieveThread(){
        var user = SampleData.createSampleUser();
        userDao.save(user);
        var thread = SampleData.createSampleThread();
        thread.setCreator(user);
        dao.save(thread);
        assertThat(dao.retrieve(thread.getId()))
                .usingRecursiveComparison()
                .isEqualTo(thread)
                .isNotSameAs(thread);


    }

}
