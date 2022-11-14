package org.kristiania.chatRoom.endPoints;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.kristiania.chatRoom.MessageThread;
import org.kristiania.chatRoom.ThreadMember;
import org.kristiania.chatRoom.database.MessageThreadDao;
import org.kristiania.chatRoom.database.ThreadMemberDao;
import org.kristiania.chatRoom.database.UserDao;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Path("/thread")
public class MessageThreadEndpoint {

    @Inject
    MessageThreadDao messageThreadDao;

    @Inject
    ThreadMemberDao threadMemberDao;

    @Inject
    UserDao userDao;


    @Path("{id}/receiver/{receiverId}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void addThread(@PathParam("id") long id, @PathParam("receiverId") long receiverId, MessageThread thread){
        var user = userDao.retrieve(id);
        thread.setCreator(user);
        messageThreadDao.save(thread);

        var sender = new ThreadMember();
        sender.setMessageThread(thread);
        sender.setUser(thread.getCreator());
        threadMemberDao.save(sender);

        var receiver = new ThreadMember();
        receiver.setMessageThread(thread);
        receiver.setUser(userDao.retrieve(receiverId));
        threadMemberDao.save(receiver);



    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MessageThread> listThreadsById(){
        return messageThreadDao.listAll();
    }



}

