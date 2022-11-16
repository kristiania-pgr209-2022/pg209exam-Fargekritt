package org.kristiania.chatRoom.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.eclipse.jetty.plus.jndi.Resource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kristiania.chatRoom.MessageThread;

import javax.naming.NamingException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

public class MessageThreadDaoTest {

    private final MessageThreadDao dao;
    private final UserDaoImpl userDao;

    private final EntityManager entityManager;



    public MessageThreadDaoTest() throws NamingException {
        var dataSource = InMemoryDataSource.createTestDataSource("testDatabase");
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

    }

    @Test
    void shouldSaveAndRetrieveThread(){
        var user = SampleData.createSampleUser(1);
        userDao.save(user);
        var thread = SampleData.createSampleThread();
        thread.setCreator(user);
        dao.save(thread);
        flush();
        assertThat(dao.retrieve(thread.getId()))
                .usingRecursiveComparison()
                .isEqualTo(thread)
                .isNotSameAs(thread);
    }

    @Test
    void shouldListAllThreads(){
        var user = SampleData.createSampleUser(1);
        userDao.save(user);
        var thread = SampleData.createSampleThread();
        thread.setCreator(user);
        dao.save(thread);
        var thread2 = SampleData.createSampleThread();
        thread2.setCreator(user);
        dao.save(thread2);
        flush();

        assertThat(dao.listAll()).extracting(MessageThread::getId)
                .contains(thread.getId(), thread2.getId());
    }

    @Test
    void shouldListAllThreadsByUserId(){
        var user = SampleData.createSampleUser(1);
        var user2 = SampleData.createSampleUser(1);
        userDao.save(user);
        userDao.save(user2);

        var thread = SampleData.createSampleThread();
        var thread2 = SampleData.createSampleThread();
        var thread3 = SampleData.createSampleThread();
        thread.setCreator(user);
        thread2.setCreator(user);
        thread3.setCreator(user2);
        dao.save(thread);
        dao.save(thread2);
        dao.save(thread3);
        flush();

        assertThat(dao.listAllByUserId(user.getId())).extracting(MessageThread::getId)
                .contains(thread.getId(), thread2.getId())
                .doesNotContain(thread3.getId());


    }

    private void flush() {
        entityManager.flush();
        entityManager.clear();
    }

}
