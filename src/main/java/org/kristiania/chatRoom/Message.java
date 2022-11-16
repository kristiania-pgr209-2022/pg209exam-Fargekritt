package org.kristiania.chatRoom;

import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
public class Message {

    @ManyToOne
    private User user;

    @ManyToOne
    private MessageThread thread;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "sent_date")
    @JsonbDateFormat(value = "yyyy-MM-dd:HH-mm-ss")
    private LocalDateTime sentDate;

    private String title;
    private String body;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public LocalDateTime getSentDate() {
        return sentDate;
    }

    public void setSentDate(LocalDateTime sentDate) {
        this.sentDate = sentDate;
    }

    public MessageThread getThread() {
        return thread;
    }

    public void setThread(MessageThread thread) {
        this.thread = thread;
    }

    @Override
    public String toString() {
        return "Message{" +
                "user=" + user +
                ", thread=" + thread +
                ", id=" + id +
                ", sentDate=" + sentDate +
                ", body='" + body + '\'' +
                '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
