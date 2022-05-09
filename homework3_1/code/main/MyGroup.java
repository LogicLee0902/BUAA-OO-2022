package main;

import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Person;

import java.util.ArrayList;
import java.util.Objects;

public class MyGroup implements Group {
    private final int id;
    private final ArrayList<Person> people = new ArrayList<Person>();
    
    public MyGroup(int id) {
        this.id = id;
    }
    
    @Override
    public int getId() {
        return id;
    }
    
    @Override
    public void addPerson(Person person) {
        if (!hasPerson(person)) {
            people.add(person);
        }
    }
    
    @Override
    public boolean hasPerson(Person person) {
        for (Person p : people) {
            if (p.equals(person)) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public int getValueSum() {
        int sum = 0;
        for (Person p : people) {
            for (Person q : people) {
                if (p.isLinked(q)) {
                    sum += p.queryValue(q);
                }
            }
        }
        return sum;
    }
    
    @Override
    public int getAgeMean() {
        if (people.size() == 0) {
            return 0;
        }
        int mean = 0;
        for (Person p : people) {
            mean += p.getAge();
        }
        mean = mean / people.size();
        return mean;
    }
    
    @Override
    public int getAgeVar() {
        if (people.size() == 0) {
            return 0;
        }
        int var = 0;
        int mean = getAgeMean();
        for (Person p : people) {
            var = var + (p.getAge() - mean) * (p.getAge() - mean);
        }
        return var;
    }
    
    @Override
    public void delPerson(Person person) {
        if (hasPerson(person)) {
            people.remove(person);
        }
    }
    
    @Override
    public int getSize() {
        return people.size();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Group)) {
            return false;
        }
        MyGroup myGroup = (MyGroup) o;
        return id == myGroup.getId();
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, people);
    }
    
    public ArrayList<Person> getPeople() {
        return people;
    }
}
