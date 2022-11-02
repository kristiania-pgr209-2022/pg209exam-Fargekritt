package org.kristiania.chatRoom.database;

import org.kristiania.chatRoom.Item;

import java.sql.SQLException;
import java.util.List;

public interface ItemDao {
    void save(Item item) throws SQLException;

    Item retrieve(long id) throws SQLException;

    List<Item> listAll() throws SQLException;
}
