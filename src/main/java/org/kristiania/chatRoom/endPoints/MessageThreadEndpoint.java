package org.kristiania.chatRoom.endPoints;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.kristiania.chatRoom.MessageThread;
import org.kristiania.chatRoom.database.MessageThreadDao;
import org.kristiania.chatRoom.database.UserDao;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Path("/thread")
public class MessageThreadEndpoint {

    @Inject
    MessageThreadDao messageThreadDao;

    @Inject
    UserDao userDao;


    @Path("{id}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MessageThread addThread(@PathParam("id") long id, MessageThread thread){
        var user = userDao.retrieve(id);
        thread.setCreator(user);
        messageThreadDao.save(thread);

        // set a timeout to check if id gets right.
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println(thread.getId());
            }
        }, 300L);


        System.out.println(thread.getId());
        return thread;
    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MessageThread> listThreadsById(){
        return messageThreadDao.listAll();
    }



}

