package org.kristiania.chatRoom.database;

import org.kristiania.chatRoom.entities.Message;
import org.kristiania.chatRoom.entities.MessageThread;
import org.kristiania.chatRoom.entities.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class SampleData {

    public static User createSampleUser(int version){
        var user = new User();
        switch (version) {
            case 1 -> {
                user.setUsername("Lulu");
                user.setFirstName("Bob");
                user.setLastName("Kåre");
                user.setGender("male");
                user.setDateOfBirth(LocalDateTime.parse("2012-01-20:20-50-42",DateTimeFormatter.ofPattern("yyyy-MM-dd:HH-mm-ss")));

            }
            case 2 -> {
                user.setUsername("exampleUser");
                user.setFirstName("exampleFirstName");
                user.setLastName("exampleLastName");
                user.setGender("male");
                user.setDateOfBirth(LocalDateTime.parse("2011-12-20:20-50-42",DateTimeFormatter.ofPattern("yyyy-MM-dd:HH-mm-ss")));

            }
            case 3 -> {
                user.setUsername("exampleUser2");
                user.setFirstName("exampleFirstName2");
                user.setLastName("exampleLastName2");
                user.setGender("male");
                user.setDateOfBirth(LocalDateTime.parse("2012-12-20:21-50-42",DateTimeFormatter.ofPattern("yyyy-MM-dd:HH-mm-ss")));
            }
        }
        return user;
    }

    public static Message createSampleMessage(int version) {
        var message = new Message();
        switch (version) {
            case 1 -> message.setBody("This is a testing body for a message");
            case 2 -> message.setBody("This is another testing body for a message");
            case 3 -> message.setBody("This is the third testing body for a message");
        }

        return message;
    }

    public static MessageThread createSampleThread() {

        return new MessageThread();
    }
}
