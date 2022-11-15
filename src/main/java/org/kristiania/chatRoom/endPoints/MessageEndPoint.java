package org.kristiania.chatRoom.endPoints;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.kristiania.chatRoom.Message;
import org.kristiania.chatRoom.dto.MessageDto;
import org.kristiania.chatRoom.database.MessageDao;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Path("/messages")
public class MessageEndPoint {

    @Inject
    MessageDao messageDao;


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


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addMessage(MessageDto messageDto){
        var message = new Message();
        message.setUser(messageDto.getUser());
        message.setThread(messageDto.getThread());
        message.setBody(messageDto.getBody());
        message.setSentDate(LocalDateTime.now());
        messageDao.save(message);
    }
}
