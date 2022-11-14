package org.kristiania.chatRoom.Dto;

import jakarta.persistence.*;
import org.kristiania.chatRoom.User;

import java.util.Set;

@Entity
@Table(name = "threads")
public class MessageThread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User creator;
    private String title;


//    @OneToMany(mappedBy = "user")
//    private Set<ThreadMember> members;

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

//    public Set<ThreadMember> getMembers() {
//        return members;
//    }
//
//    public void setMembers(Set<ThreadMember> members) {
//        this.members = members;
//    }

}
