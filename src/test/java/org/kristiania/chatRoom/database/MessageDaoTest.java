package org.kristiania.chatRoom.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.eclipse.jetty.plus.jndi.Resource;
import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.naming.NamingException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Properties;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageDaoTest {

    private final MessageDao messageDao;
    private final UserDao userDao;

    private final MessageThreadDao messageThreadDao;
    private final EntityManager entityManager;


    public MessageDaoTest() throws NamingException {
        JdbcDataSource datasource = InMemoryDataSource.createTestDataSource("daoTestDb");

        new Resource("jdbc/dataSource", datasource);
        this.entityManager = Persistence.createEntityManagerFactory("ChatRoom").createEntityManager();
        messageThreadDao = new MessageThreadDao(entityManager);
        messageDao = new MessageDaoImpl(entityManager);
        userDao = new UserDaoImpl(entityManager);
    }

    @BeforeEach
    void setUp() {
        entityManager.getTransaction().begin();
    }

    @AfterEach
    void tearDown() throws SQLException {
        entityManager.getTransaction().rollback();
    }

    @Test
    void shouldRetrieveSavedMessage() {
        var user = SampleData.createSampleUser(1);
        userDao.save(user);
        var messageThread = SampleData.createSampleThread();
        messageThread.setCreator(user);
        messageThreadDao.save(messageThread);
        var message = SampleData.createSampleMessage(2);
        message.setUser(user);
        message.setThread(messageThread);
        message.setSentDate(LocalDate.now());
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
