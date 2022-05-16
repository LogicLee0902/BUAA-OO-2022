package main;

import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Person;

import java.util.ArrayList;
import java.util.Objects;

public class MyGroup implements Group {
    private final int id;
    private final ArrayList<Person> people = new ArrayList<Person>();
    
    private int sumAge = 0;
    
    private int valueSum = 0;
    
    private int socialValue = 0;
    
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
            sumAge += person.getAge();
            for (Person other : ((MyPerson)person).getValue().keySet()) {
                if (hasPerson(other)) {
                    valueSum += 2 * ((MyPerson)person).getValue().get(other);
                }
            }
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
        return valueSum;
    }
    
    @Override
    public int getAgeMean() {
        if (people.size() == 0) {
            return 0;
        }
        int mean;
        mean = sumAge / people.size();
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
        return var / people.size();
    }
    
    @Override
    public void delPerson(Person person) {
        if (hasPerson(person)) {
            sumAge -= person.getAge();
            people.remove(person);
            for (Person other : ((MyPerson)person).getValue().keySet()) {
                if (hasPerson(other)) {
                    valueSum -= 2 * ((MyPerson)person).getValue().get(other);
                }
            }
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
    
    public void addValue(int value) {
        valueSum = valueSum + 2 * value;
    }
    
    public int getSocialValue() {
        return socialValue;
    }
    
    public void addSocialValue(int value) {
        this.socialValue += value;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id, people);
    }
    
    public ArrayList<Person> getPeople() {
        return people;
    }
}
