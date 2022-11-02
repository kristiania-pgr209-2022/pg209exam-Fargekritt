package org.kristiania.chatRoom.endPoints;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.kristiania.chatRoom.Message;
import org.kristiania.chatRoom.database.MessageDao;

import java.util.List;

@Path("/messages")
public class MessageEndPoint {

    @Inject
    MessageDao dao;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Message> listAll(){
        return dao.listAll();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addMessage(Message message){

    }
}
