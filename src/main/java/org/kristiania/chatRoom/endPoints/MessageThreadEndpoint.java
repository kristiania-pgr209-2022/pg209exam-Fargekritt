package org.kristiania.chatRoom.endPoints;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import org.kristiania.chatRoom.MessageThread;
import org.kristiania.chatRoom.database.MessageThreadDao;
import org.kristiania.chatRoom.database.UserDao;

@Path("/thread")
public class MessageThreadEndpoint {

    @Inject
    MessageThreadDao messageThreadDao;

    @Inject
    UserDao userDao;


    @Path("{id}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addThread(@PathParam("id") Long id, MessageThread thread){
        var user = userDao.retrieve(id);
        thread.setCreator(user);
        messageThreadDao.save(thread);
    }



}

