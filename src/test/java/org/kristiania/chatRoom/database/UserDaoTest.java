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

public class UserDaoTest {

    private final UserDao dao;

    private final EntityManager entityManager;



    public UserDaoTest() throws NamingException {
        var dataSource = InMemoryDataSource.createTestDataSource();
        new Resource("jdbc/dataSource", dataSource);
        this.entityManager = Persistence.createEntityManagerFactory("ChatRoom").createEntityManager();

        dao = new UserDaoImpl(entityManager);
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
    void shouldRetrieveSavedUser(){
        var user = SampleData.createSampleUser(1);
        dao.save(user);
        flush();
        assertThat(dao.retrieve(user.getId()))
                .usingRecursiveComparison()
                .isEqualTo(user)
                .isNotSameAs(user);
    }


    private void flush() {
        entityManager.flush();
        entityManager.clear();
    }


}
