package org.kristiania.chatRoom.database;

import org.kristiania.chatRoom.entities.MessageThread;
import org.kristiania.chatRoom.entities.ThreadMember;
import org.kristiania.chatRoom.entities.User;

import java.util.List;

public interface ThreadMemberDao {
    ThreadMember retrieve(long id);

    List<MessageThread> findByUser(long userId);

    void save(ThreadMember messageThread);

    List<User> findByThread(long id);
}
