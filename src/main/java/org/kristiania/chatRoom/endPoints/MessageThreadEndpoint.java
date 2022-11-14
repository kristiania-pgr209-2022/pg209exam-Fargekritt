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


    @Path("{id}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void addThread(@PathParam("id") long id, MessageThread thread){
        var user = userDao.retrieve(id);
        thread.setCreator(user);
        messageThreadDao.save(thread);

        var threadMember = new ThreadMember();
        threadMember.setMessageThread(thread);
        threadMember.setUser(thread.getCreator());

        threadMemberDao.save(threadMember);


    }


    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MessageThread> listThreadsById(){
        return messageThreadDao.listAll();
    }



}

