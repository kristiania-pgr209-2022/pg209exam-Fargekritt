package org.kristiania.chatRoom.endPoints;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.kristiania.chatRoom.MessageThread;
import org.kristiania.chatRoom.User;
import org.kristiania.chatRoom.database.ThreadMemberDao;
import org.kristiania.chatRoom.database.UserDao;

import java.util.List;

@Path("/users")
public class UserEndPoint {

    @Inject
    private UserDao userDao;

    @Inject
    private ThreadMemberDao threadMemberDao;

    //localhost:8080/api/users/{id}
    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public User getUser( @PathParam("id") String id){
        return userDao.retrieve(Long.parseLong(id));
    }

    //localhost:8080/api/users
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> listUsers(){
        return userDao.listAll();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addUser(User user){
        userDao.save(user);
    }

    @Path("{id}/threads")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MessageThread> listAllThreads(@PathParam("id") long id){
        return threadMemberDao.findByUser(id);
    }

}
