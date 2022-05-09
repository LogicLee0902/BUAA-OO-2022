package main;

import com.oocourse.spec1.exceptions.EqualGroupIdException;
import com.oocourse.spec1.exceptions.EqualPersonIdException;
import com.oocourse.spec1.exceptions.EqualRelationException;
import com.oocourse.spec1.exceptions.GroupIdNotFoundException;
import com.oocourse.spec1.exceptions.PersonIdNotFoundException;
import com.oocourse.spec1.exceptions.RelationNotFoundException;
import com.oocourse.spec1.main.Group;
import com.oocourse.spec1.main.Network;
import com.oocourse.spec1.main.Person;

import java.util.ArrayList;
import java.util.HashMap;

public class MyNetwork implements Network {
    private final ArrayList<Person> people = new ArrayList<Person>();
    private final ArrayList<Group> groups = new ArrayList<Group>();
    private final HashMap<Person, Community> communities = new HashMap<Person, Community>();
    
    private int blockCount = 0;
    
    public MyNetwork() {
    }
    
    @Override
    public boolean contains(int id) {
        for (Person person : people) {
            if (person.getId() == id) {
                return true;
            }
        }
        return false;
    }
    
    @Override
    public Person getPerson(int id) {
        for (Person person : people) {
            if (person.getId() == id) {
                return person;
            }
        }
        return null;
    }
    
    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        for (Person value : people) {
            if (value.equals(person)) {
                throw new MyEqualPersonIdException(person.getId());
            }
        }
        people.add(person);
        blockCount++;
        Community community = new Community(person);
        communities.put(person, community);
    }
    
    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        MyPerson p1 = findPersonById(id1);
        MyPerson p2 = findPersonById(id2);
        if (p1 == null) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (p2 == null) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (p1.isLinked(p2)) {
            throw new MyEqualRelationException(id1, id2);
        } else {
            p1.addAcquaintance(p2);
            p2.addAcquaintance(p1);
            p1.addValue(value);
            p2.addValue(value);
            // not stand for the same block
            Community c1 = communities.get(p1);
            Community c2 = communities.get(p2);
            if (!c1.equals(c2)) {
                blockCount--;
                int amount = c1.getHighest().getAmount() + c2.getHighest().getAmount();
                if (c1.getHighest().getAmount() < c2.getHighest().getAmount()) {
                    c2.getHighest().setHigher(c1);
                    c1.getHighest().setAmount(amount);
                } else {
                    c1.getHighest().setHigher(c2);
                    c2.getHighest().setAmount(amount);
                }
            }
        }
    }
    
    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        MyPerson p1 = findPersonById(id1);
        MyPerson p2 = findPersonById(id2);
        if (p1 == null) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (p2 == null) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (!p1.isLinked(p2)) {
            throw new MyRelationNotFoundException(id1, id2);
        } else {
            return p1.queryValue(p2);
        }
    }
    
    @Override
    public int queryPeopleSum() {
        return people.size();
    }
    
    @Override
    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        MyPerson p1 = findPersonById(id1);
        MyPerson p2 = findPersonById(id2);
        if (p1 == null) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (p2 == null) {
            throw new MyPersonIdNotFoundException(id2);
        } else {
            return communities.get(p1).equals(communities.get(p2));
        }
    }
    
    @Override
    public int queryBlockSum() {
        return blockCount;
    }
    
    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        for (Group value : groups) {
            if (value.equals(group)) {
                throw new MyEqualGroupIdException(group.getId());
            }
        }
        groups.add(group);
    }
    
    @Override
    public Group getGroup(int id) {
        for (Group group : groups) {
            if (group.getId() == id) {
                return group;
            }
        }
        return null;
    }
    
    @Override
    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException,
            EqualPersonIdException {
        MyPerson person = findPersonById(id1);
        MyGroup group = findGroupById(id2);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (person == null) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (group.hasPerson(person)) {
            throw new MyEqualPersonIdException(id1);
        } else if (group.getPeople().size() < 1111) {
            group.getPeople().add(person);
        }
    }
    
    @Override
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException,
            EqualPersonIdException {
        MyGroup group = findGroupById(id2);
        MyPerson person = findPersonById(id1);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (person == null) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!group.hasPerson(person)) {
            throw new MyEqualPersonIdException(id1);
        } else {
            group.getPeople().remove(person);
        }
    }
    
    private MyPerson findPersonById(int id) {
        for (Person person : people) {
            if (person.getId() == id) {
                return (MyPerson) person;
            }
        }
        return null;
    }
    
    private MyGroup findGroupById(int id) {
        for (Group group : groups) {
            if (group.getId() == id) {
                return (MyGroup) group;
            }
        }
        return null;
    }
}
