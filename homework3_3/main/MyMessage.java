package main;

import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

import java.util.Objects;

public class MyMessage implements Message {
    private final int id;
    private final int socialValue;
    private final int type;
    private final Person sender;
    private final Person receiver;
    private final Group group;
    
    public MyMessage(int messageId, int messageSocialValue,
                     Person messagePerson1, Person messagePerson2) {
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.sender = messagePerson1;
        this.receiver = messagePerson2;
        this.type = 0;
        this.group = null;
    }
    
    public MyMessage(int messageId, int messageSocialValue,
                     Person messagePerson1, Group messageGroup) {
        this.id = messageId;
        this.socialValue = messageSocialValue;
        this.sender = messagePerson1;
        this.group = messageGroup;
        this.receiver = null;
        this.type = 1;
    }
    
    @Override
    public int getType() {
        return type;
    }
    
    @Override
    public int getId() {
        return id;
    }
    
    @Override
    public int getSocialValue() {
        return socialValue;
    }
    
    @Override
    public Person getPerson1() {
        return sender;
    }
    
    @Override
    public Person getPerson2() {
        return receiver;
    }
    
    @Override
    public Group getGroup() {
        return group;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MyMessage)) {
            return false;
        }
        MyMessage myMessage = (MyMessage) o;
        return id == myMessage.id;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, socialValue, type, sender, receiver, group);
    }
}
