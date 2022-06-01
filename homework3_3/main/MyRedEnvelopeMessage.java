package main;

import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;

public class MyRedEnvelopeMessage extends MyMessage implements RedEnvelopeMessage {
    
    private final int money;
    
    public MyRedEnvelopeMessage(int messageId, int money,
                                Person messagePerson1, Person messagePerson2) {
        super(messageId, money * 5, messagePerson1, messagePerson2);
        this.money = money;
    }
    
    public MyRedEnvelopeMessage(int messageId, int money,
                                Person messagePerson1, Group messageGroup) {
        super(messageId, money * 5, messagePerson1, messageGroup);
        this.money = money;
    }
    
    @Override
    public int getMoney() {
        return money;
    }
}
