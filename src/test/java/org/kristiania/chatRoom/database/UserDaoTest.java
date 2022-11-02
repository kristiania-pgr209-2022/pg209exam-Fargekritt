package org.kristiania.chatRoom.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Persistence;
import org.eclipse.jetty.plus.jndi.Resource;

import org.h2.jdbcx.JdbcDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.kristiania.chatRoom.User;

import javax.naming.NamingException;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

public class UserDaoTest {

    private final UserDao dao;

    private final EntityManager entityManager;


    public UserDaoTest() throws NamingException {
        JdbcDataSource datasource = InMemoryDataSource.createTestDataSource();

        new Resource("jdbc/dataSource", datasource);
        this.entityManager = Persistence.createEntityManagerFactory("ChatRoom").createEntityManager();

        dao = new UserDaoImpl(entityManager);
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
    void shouldRetrieveSavedUser(){
        var user = sampleUser();
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

    private User sampleUser() {
        var user = new User();
        user.setUsername("Lulu");
        user.setFirstName("Bob");
        user.setLastName("Kåre");
        user.setGender("male");
        user.setDateOfBirth(LocalDate.of(2012, 1, 20));
        return user;
    }

}
