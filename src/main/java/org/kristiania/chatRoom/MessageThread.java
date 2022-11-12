package org.kristiania.chatRoom;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "threads")
public class MessageThread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private User creator;


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

//    public Set<ThreadMember> getMembers() {
//        return members;
//    }
//
//    public void setMembers(Set<ThreadMember> members) {
//        this.members = members;
//    }

}
