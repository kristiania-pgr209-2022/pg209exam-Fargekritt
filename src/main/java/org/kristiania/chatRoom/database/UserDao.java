package org.kristiania.chatRoom.database;

import org.kristiania.chatRoom.entities.User;

import java.util.List;

public interface UserDao {
    void save(User user);

    User retrieve(long id);

    List<User> listAll();
}
