package org.kristiania.chatRoom.database;

import org.kristiania.chatRoom.Message;
import org.kristiania.chatRoom.User;

import java.time.LocalDate;

public class SampleData {

    public static User createSampleUser(){
        var user = new User();
        user.setUsername("Lulu");
        user.setFirstName("Bob");
        user.setLastName("Kåre");
        user.setGender("male");
        user.setDateOfBirth(LocalDate.of(2012, 1, 20));
        return user;
    }

    public static Message createSampleMessage() {
        var message = new Message();
        message.setBody("This is a testing body for a message");
        return message;
    }
}
