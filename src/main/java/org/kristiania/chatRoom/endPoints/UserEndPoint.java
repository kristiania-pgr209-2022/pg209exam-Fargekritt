package org.kristiania.chatRoom.endPoints;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.kristiania.chatRoom.entities.MessageThread;
import org.kristiania.chatRoom.entities.User;
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
    public User getUser( @PathParam("id") long id){
        return userDao.retrieve(id);
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

    @Path("{id}")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void editUser(@PathParam("id") long id, User newUser){
        var user = userDao.retrieve(id);
        user.setFirstName(newUser.getFirstName());
        user.setLastName(newUser.getLastName());
        user.setGender(newUser.getGender());
        user.setUsername(newUser.getUsername());
        user.setEmail(newUser.getEmail());
        userDao.save(user);
    }


    @Path("{id}/threads")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MessageThread> listAllThreads(@PathParam("id") long id){
        return threadMemberDao.findByUser(id);
    }

}
