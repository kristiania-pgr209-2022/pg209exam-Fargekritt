package org.kristiania.chatRoom.dto;

import org.kristiania.chatRoom.entities.User;

import java.util.List;

public class MessageThreadDto {


    private String threadTitle;


    public String getMessage() {
        return message;
    }
    private String message;

    private User creator;

    private List<User> members;

    private User user;

    public void setMessage(String message) {
        this.message = message;
    }

    public String getThreadTitle() {
        return threadTitle;
    }

    public void setThreadTitle(String threadTitle) {
        this.threadTitle = threadTitle;
    }


    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getMembers() {
        return members;
    }

    public void setMembers(List<User> members) {
        this.members = members;
    }
}
