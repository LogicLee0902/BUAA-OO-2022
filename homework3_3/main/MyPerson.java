package main;

import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MyPerson implements Person {
    
    private final int id;
    private final String name;
    private final int age;
    private int money;
    private int socialValue;
    private final LinkedList<Message> messages = new LinkedList<Message>();
    private final HashMap<Person, Integer> acquaintance = new HashMap<Person, Integer>();
    private final HashMap<Person, Integer> value = new HashMap<Person, Integer>();
    
    private final ArrayList<Integer> allGroups = new ArrayList<Integer>();
    
    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }
    
    @Override
    public int getId() {
        return id;
    }
    
    @Override
    public String getName() {
        return name;
    }
    
    @Override
    public int getAge() {
        return age;
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Person)) {
            return false;
        }
        MyPerson myPerson = (MyPerson) obj;
        return id == myPerson.getId();
    }
    
    @Override
    public boolean isLinked(Person person) {
        if (id == person.getId()) {
            return true;
        }
        return acquaintance.containsKey(person);
    }
    
    @Override
    public int queryValue(Person person) {
        if (acquaintance.containsKey(person)) {
            //System.out.println(person.getId())
            return value.get(person);
        }
        return 0;
    }
    
    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }
    
    @Override
    public void addSocialValue(int num) {
        socialValue += num;
    }
    
    @Override
    public int getSocialValue() {
        return socialValue;
    }
    
    @Override
    public List<Message> getMessages() {
        return messages;
    }
    
    @Override
    public List<Message> getReceivedMessages() {
        return messages.subList(0, Math.min(4, messages.size()));
    }
    
    @Override
    public void addMoney(int num) {
        money += num;
    }
    
    @Override
    public int getMoney() {
        return money;
    }
    
    public void addAcquaintance(Person person) {
        this.acquaintance.put(person, person.getId());
    }
    
    public void addValue(Person person, int value) {
        this.value.put(person, value);
    }
    
    public ArrayList<Integer> getAllGroups() {
        return allGroups;
    }
    
    public void addGroupId(int id) {
        allGroups.add(id);
    }
    
    public void removeGroupId(int id) {
        allGroups.remove(Integer.valueOf(id));
    }
    
    public HashMap<Person, Integer> getValue() {
        return value;
    }
}
