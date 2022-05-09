package main;

import com.oocourse.spec1.main.Person;

import java.util.ArrayList;

public class MyPerson implements Person {
    
    private final int id;
    private final String name;
    private final int age;
    private final ArrayList<Person> acquaintance = new ArrayList<Person>();
    private final ArrayList<Integer> value = new ArrayList<>();
    
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
        for (Person acquaint : acquaintance) {
            if (acquaint.getId() == person.getId()) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int queryValue(Person person) {
        for (int i = 0; i < acquaintance.size(); ++i) {
            if (acquaintance.get(i).getId() == person.getId()) {
                return value.get(i);
            }
        }
        return 0;
    }
    
    @Override
    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }
    
    public void addAcquaintance(Person person) {
        this.acquaintance.add(person);
    }
    
    public void addValue(int value) {
        this.value.add(value);
    }
    
    public ArrayList<Person> getAcquaintance() {
        return acquaintance;
    }
}
