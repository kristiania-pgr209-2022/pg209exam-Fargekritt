package org.kristiania.chatRoom.endPoints;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.kristiania.chatRoom.MessageThread;
import org.kristiania.chatRoom.database.MessageThreadDao;
import org.kristiania.chatRoom.database.UserDao;

import java.util.List;

@Path("/thread")
public class MessageThreadEndpoint {

    @Inject
    MessageThreadDao messageThreadDao;

    @Inject
    UserDao userDao;


    @Path("{id}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addThread(@PathParam("id") long id, MessageThread thread){
        var user = userDao.retrieve(id);
        thread.setCreator(user);
        messageThreadDao.save(thread);
    }


    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MessageThread> listThreadsById(@PathParam("id") long id){
        return messageThreadDao.listAllById(id);
    }



}

