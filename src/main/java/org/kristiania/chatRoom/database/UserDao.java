package org.kristiania.chatRoom.database;

import org.kristiania.chatRoom.User;

public interface UserDao {
    void save(User user);

    User retrieve(long id);
}
