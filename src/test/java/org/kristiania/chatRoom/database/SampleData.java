package org.kristiania.chatRoom.database;

import org.kristiania.chatRoom.Message;
import org.kristiania.chatRoom.Dto.MessageThread;
import org.kristiania.chatRoom.User;

import java.time.LocalDate;

public class SampleData {

    public static User createSampleUser(int version){
        var user = new User();
        switch (version) {
            case 1 -> {
                user.setUsername("Lulu");
                user.setFirstName("Bob");
                user.setLastName("Kåre");
                user.setGender("male");
                user.setDateOfBirth(LocalDate.of(2012, 1, 20));

            }
            case 2 -> {
                user.setUsername("exampleUser");
                user.setFirstName("exampleFirstName");
                user.setLastName("exampleLastName");
                user.setGender("male");
                user.setDateOfBirth(LocalDate.of(2011, 12, 20));

            }
            case 3 -> {
                user.setUsername("exampleUser2");
                user.setFirstName("exampleFirstName2");
                user.setLastName("exampleLastName2");
                user.setGender("male");
                user.setDateOfBirth(LocalDate.of(2010, 10, 20));
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
