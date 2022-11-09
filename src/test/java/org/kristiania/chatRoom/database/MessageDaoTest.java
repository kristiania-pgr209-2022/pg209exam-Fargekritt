package org.kristiania.chatRoom.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.eclipse.jetty.plus.jndi.Resource;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.NamingException;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageDaoTest {

    private final MessageDao messageDao;
    private final UserDao userDao;
    private final EntityManager entityManager;


    public MessageDaoTest() throws NamingException {
        JdbcDataSource datasource = InMemoryDataSource.createTestDataSource();

        new Resource("jdbc/dataSource", datasource);
        this.entityManager = Persistence.createEntityManagerFactory("ChatRoom").createEntityManager();

        messageDao = new MessageDaoImpl(entityManager);
        userDao = new UserDaoImpl(entityManager);
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
    void shouldRetrieveSavedMessage() {
        var user = SampleData.createSampleUser();
        userDao.save(user);
        var message = SampleData.createSampleMessage();
        message.setUser(user);
        messageDao.save(message);
        flush();
        assertThat(messageDao.retrieve(message.getId()))
                .usingRecursiveComparison()
                .isEqualTo(message)
                .isNotSameAs(message);
    }

    private void flush() {
        entityManager.flush();
        entityManager.clear();
    }
}
