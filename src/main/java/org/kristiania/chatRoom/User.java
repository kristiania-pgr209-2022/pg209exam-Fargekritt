package org.kristiania.chatRoom;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import jakarta.json.bind.annotation.JsonbDateFormat;
import jakarta.persistence.*;
import org.kristiania.chatRoom.localDateFormatting.LocalDateDeserializer;
import org.kristiania.chatRoom.localDateFormatting.LocalDateSerializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private long id;

    @Column(name = "date_of_birth")
    @JsonDeserialize(using = LocalDateDeserializer.class)
    @JsonSerialize(using = LocalDateSerializer.class)
    @JsonbDateFormat(value = "yyyy-MM-dd:HH-mm-ss")
    private LocalDateTime dateOfBirth;
    private String gender;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "first_name")
    private String firstName;
    private String username;

//    @OneToMany(mappedBy = "messageThread")
//    private Set<ThreadMember> members;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDateOfBirth(LocalDateTime dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDateTime getDateOfBirth() {
        return dateOfBirth;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

//    public Set<ThreadMember> getMembers() {
//        return members;
//    }
//
//    public void setMembers(Set<ThreadMember> members) {
//        this.members = members;
//    }

}
