package org.kristiania.chatRoom.endPoints;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.kristiania.chatRoom.Message;
import org.kristiania.chatRoom.database.MessageDao;
import org.kristiania.chatRoom.database.UserDao;

import java.util.List;

@Path("/messages")
public class MessageEndPoint {

    @Inject
    MessageDao dao;

    @Inject
    UserDao userDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> listAll(){
        return dao.listAll();
    }


    @Path("user/{id}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addMessage(@PathParam("id") long id, Message message){
        var user = userDao.retrieve(id);
        message.setUser(user);
        dao.save(message);
    }
}
