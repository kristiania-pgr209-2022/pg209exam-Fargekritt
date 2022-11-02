package org.kristiania.store;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.kristiania.store.database.ItemDao;
import org.kristiania.store.database.ItemDaoImpl;

import java.sql.SQLException;
import java.util.List;

@Path("/items")
public class ItemEndPoint {

    @Inject
    private ItemDao dao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Item> listItems() throws SQLException {
        return dao.listAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addItem(Item item) throws SQLException {
        dao.save(item);
    }
}
