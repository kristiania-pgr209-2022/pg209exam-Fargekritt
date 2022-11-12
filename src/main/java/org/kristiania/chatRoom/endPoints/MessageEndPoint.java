package org.kristiania.chatRoom.endPoints;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.kristiania.chatRoom.Message;
import org.kristiania.chatRoom.database.MessageDao;
import org.kristiania.chatRoom.database.MessageThreadDao;
import org.kristiania.chatRoom.database.UserDao;

import java.util.List;

@Path("/messages")
public class MessageEndPoint {

    @Inject
    MessageDao messageDao;

    @Inject
    UserDao userDao;

    @Inject
    MessageThreadDao threadDao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> listAll(){
        return messageDao.listAll();
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Message getUser(@PathParam("id") int id){
        return messageDao.retrieve(id);
    }


    @Path("user/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> getMessageByUserId(@PathParam("id") long id){
        return messageDao.findByUser(id);
    }

    @Path("thread/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> getMessagesByThreadId(@PathParam("id") long id){
        return messageDao.findByThreadId(id);
    }

    @Path("user/{id}/thread/{threadId}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addMessage(@PathParam("id") long id,@PathParam("threadId") long threadId, Message message){
        var user = userDao.retrieve(id);
        var thread = threadDao.retrieve(threadId);
        message.setUser(user);
        message.setThread(thread);
        messageDao.save(message);
    }
}
