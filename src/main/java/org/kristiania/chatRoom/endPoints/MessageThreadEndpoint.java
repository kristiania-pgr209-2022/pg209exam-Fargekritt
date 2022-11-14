package org.kristiania.chatRoom.endPoints;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.kristiania.chatRoom.*;
import org.kristiania.chatRoom.MessageThread;
import org.kristiania.chatRoom.dto.MessageThreadDto;
import org.kristiania.chatRoom.database.MessageDao;
import org.kristiania.chatRoom.database.MessageThreadDao;
import org.kristiania.chatRoom.database.ThreadMemberDao;
import org.kristiania.chatRoom.database.UserDao;

import java.time.LocalDate;
import java.util.List;

@Path("/thread")
public class MessageThreadEndpoint {

    @Inject
    MessageThreadDao messageThreadDao;

    @Inject
    ThreadMemberDao threadMemberDao;

    @Inject
    MessageDao messageDao;

    @Inject
    UserDao userDao;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addThread(MessageThreadDto messageThreadDto){
        var user = messageThreadDto.getCreator();

        var thread = new MessageThread();
        thread.setCreator(user);
        thread.setTitle(messageThreadDto.getTitle());
        messageThreadDao.save(thread);

        var message = new Message();
        message.setThread(thread);
        message.setUser(user);
        message.setBody(messageThreadDto.getMessage());
        message.setSentDate(LocalDate.now());
        messageDao.save(message);

        var sender = new ThreadMember();
        sender.setMessageThread(thread);
        sender.setUser(thread.getCreator());
        threadMemberDao.save(sender);

        var receiver = new ThreadMember();
        receiver.setMessageThread(thread);
        receiver.setUser(userDao.retrieve(messageThreadDto.getReceiverId()));
        threadMemberDao.save(receiver);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MessageThread> listAllThreads(){
        return messageThreadDao.listAll();
    }

    @Path("{id}/members")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> listThreadMembers(@PathParam("id") long id){
        return threadMemberDao.findByThread(id);
    }

}

