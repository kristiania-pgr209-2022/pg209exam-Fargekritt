package org.kristiania.chatRoom.endPoints;

import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import org.kristiania.chatRoom.entities.Message;
import org.kristiania.chatRoom.entities.MessageThread;
import org.kristiania.chatRoom.dto.MemberDto;
import org.kristiania.chatRoom.dto.MessageThreadDto;
import org.kristiania.chatRoom.database.MessageDao;
import org.kristiania.chatRoom.database.MessageThreadDao;
import org.kristiania.chatRoom.database.ThreadMemberDao;
import org.kristiania.chatRoom.entities.ThreadMember;
import org.kristiania.chatRoom.entities.User;

import java.time.LocalDateTime;
import java.util.List;

@Path("/thread")
public class MessageThreadEndpoint {

    @Inject
    MessageThreadDao messageThreadDao;

    @Inject
    ThreadMemberDao threadMemberDao;

    @Inject
    MessageDao messageDao;


    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addThread(MessageThreadDto messageThreadDto) {
        var user = messageThreadDto.getCreator();

        var thread = new MessageThread();
        thread.setCreator(user);
        thread.setTitle(messageThreadDto.getThreadTitle());
        messageThreadDao.save(thread);

        var message = new Message();
        message.setTitle(messageThreadDto.getMessageTitle());
        message.setThread(thread);
        message.setUser(user);
        message.setBody(messageThreadDto.getMessage());
        message.setSentDate(LocalDateTime.now());
        messageDao.save(message);

        var sender = new ThreadMember();
        sender.setMessageThread(thread);
        sender.setUser(thread.getCreator());
        threadMemberDao.save(sender);

        for (User member : messageThreadDto.getMembers()) {
            var receiver = new ThreadMember();
            receiver.setMessageThread(thread);
            receiver.setUser(member);
            threadMemberDao.save(receiver);
        }

    }

    @Path("/member")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public void addMember(MemberDto memberDto) {

        var member = new ThreadMember();
        member.setMessageThread(memberDto.getThread());
        member.setUser(memberDto.getUser());
        threadMemberDao.save(member);
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<MessageThread> listAllThreads() {
        return messageThreadDao.listAll();
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public MessageThread getThreadById(@PathParam("id") long id){
        return messageThreadDao.retrieve(id);
    }

    @Path("{id}/members")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<User> listThreadMembers(@PathParam("id") long id) {
        return threadMemberDao.findByThread(id);
    }

}

