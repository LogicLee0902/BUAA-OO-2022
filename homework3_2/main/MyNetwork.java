package main;

import com.oocourse.spec2.exceptions.EqualGroupIdException;
import com.oocourse.spec2.exceptions.EqualMessageIdException;
import com.oocourse.spec2.exceptions.EqualPersonIdException;
import com.oocourse.spec2.exceptions.EqualRelationException;
import com.oocourse.spec2.exceptions.GroupIdNotFoundException;
import com.oocourse.spec2.exceptions.MessageIdNotFoundException;
import com.oocourse.spec2.exceptions.PersonIdNotFoundException;
import com.oocourse.spec2.exceptions.RelationNotFoundException;
import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Network;
import com.oocourse.spec2.main.Person;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> people = new HashMap<Integer, Person>();
    private final HashMap<Integer, Group> groups = new HashMap<Integer, Group>();
    private final HashMap<Person, Person> communities = new HashMap<Person, Person>();
    private final HashMap<Person, Integer> amounts = new HashMap<Person, Integer>();
    private final HashMap<Integer, Message> messages = new HashMap<Integer, Message>();
    
    private int blockCount = 0;
    
    public MyNetwork() {
    }
    
    private Person find(Person person, HashMap<Person, Person> communities) {
        Person temp = person;
        if (!communities.containsKey(temp)) {
            return person;
        }
        while (!communities.get(temp).equals(temp)) {
            temp = communities.get(temp);
        }
        Person updated = person;
        while (!communities.get(updated).equals(updated)) {
            updated = communities.get(updated);
            communities.put(updated, temp);
        }
        return temp;
    }
    
    @Override
    public boolean contains(int id) {
        return people.containsKey(id);
    }
    
    @Override
    public Person getPerson(int id) {
        if (contains(id)) {
            return people.get(id);
        }
        return null;
    }
    
    @Override
    public void addPerson(Person person) throws EqualPersonIdException {
        if (people.containsKey(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        }
        people.put(person.getId(), person);
        blockCount++;
        communities.put(person, person);
        amounts.put(person, 1);
    }
    
    @Override
    public void addRelation(int id1, int id2, int value)
            throws PersonIdNotFoundException, EqualRelationException {
        MyPerson p1 = (MyPerson) people.get(id1);
        MyPerson p2 = (MyPerson) people.get(id2);
        if (p1 == null) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (p2 == null) {
            throw new MyPersonIdNotFoundException(id2);
        } else if (p1.isLinked(p2)) {
            throw new MyEqualRelationException(id1, id2);
        } else {
            p1.addAcquaintance(p2);
            p2.addAcquaintance(p1);
            p1.addValue(p2, value);
            p2.addValue(p1, value);
            // not stand for the same block
            Person lord1 = find(p1, this.communities);
            Person lord2 = find(p2, this.communities);
            if (!lord1.equals(lord2)) {
                blockCount--;
                communities.put(lord1, lord2);
                amounts.put(lord2, amounts.get(lord1) + amounts.get(lord2));
            }
            for (Integer index : p1.getAllGroups()) {
                MyGroup group = (MyGroup) groups.get(index);
                // System.out.println("Id : " + group.getId());
                if (group.hasPerson(p2)) {
                    // System.out.println("person Id : " + p2.getId());
                    group.addValue(value);
                }
            }
        }
    }
    
    @Override
    public int queryValue(int id1, int id2)
            throws PersonIdNotFoundException, RelationNotFoundException {
        MyPerson p1 = (MyPerson) people.get(id1);
        MyPerson p2 = (MyPerson) people.get(id2);
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
        MyPerson p1 = (MyPerson) people.get(id1);
        MyPerson p2 = (MyPerson) people.get(id2);
        if (p1 == null) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (p2 == null) {
            throw new MyPersonIdNotFoundException(id2);
        } else {
            return find(p1, this.communities).equals(find(p2, this.communities));
        }
    }
    
    @Override
    public int queryBlockSum() {
        return blockCount;
    }
    
    @Override
    public int queryLeastConnection(int id) throws PersonIdNotFoundException {
        if (!people.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        int sum = 0;
        int cnt = 1;
        PriorityQueue<Edge> edges =
                new PriorityQueue<>(Comparator.comparingInt(Edge::getValue));
        HashSet<Person> visited = new HashSet<Person>();
        MyPerson root = (MyPerson) people.get(id);
        // total of the connected dots
        int total = amounts.get(find(root, communities));
        visited.add(root);
        for (Person p : root.getValue().keySet()) {
            edges.add(new Edge(root.queryValue(p), root, p));
        }
        while (cnt < total && edges.size() > 0) {
            Edge e = edges.poll();
            while (e != null && visited.contains(e.getP2())) {
                e = edges.poll();
            }
            if (e == null) {
                break;
            }
            MyPerson person = (MyPerson) (e.getP2());
            visited.add(person);
            sum += e.getValue();
            cnt++;
            for (Person p : person.getValue().keySet()) {
                edges.add(new Edge(person.queryValue(p), person, p));
            }
        }
        return sum;
    }
    
    @Override
    public void addGroup(Group group) throws EqualGroupIdException {
        if (groups.containsKey(group.getId())) {
            throw new MyEqualGroupIdException(group.getId());
        }
        groups.put(group.getId(), group);
    }
    
    @Override
    public Group getGroup(int id) {
        if (groups.containsKey(id)) {
            return groups.get(id);
        }
        return null;
    }
    
    @Override
    public void addToGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException,
            EqualPersonIdException {
        MyPerson person = (MyPerson) people.get(id1);
        MyGroup group = (MyGroup) groups.get(id2);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (person == null) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (group.hasPerson(person)) {
            throw new MyEqualPersonIdException(id1);
        } else if (group.getSize() < 1111) {
            group.addPerson(person);
            person.addGroupId(id2);
        }
    }
    
    @Override
    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        MyGroup group = (MyGroup) groups.get(id);
        return group.getSize();
    }
    
    @Override
    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        MyGroup group = (MyGroup) groups.get(id);
        return group.getValueSum();
    }
    
    @Override
    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!groups.containsKey(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        MyGroup group = (MyGroup) groups.get(id);
        return group.getAgeVar();
    }
    
    @Override
    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException,
            EqualPersonIdException {
        MyPerson person = (MyPerson) people.get(id1);
        MyGroup group = (MyGroup) groups.get(id2);
        if (group == null) {
            throw new MyGroupIdNotFoundException(id2);
        } else if (person == null) {
            throw new MyPersonIdNotFoundException(id1);
        } else if (!group.hasPerson(person)) {
            throw new MyEqualPersonIdException(id1);
        } else {
            group.delPerson(person);
            person.removeGroupId(id2);
        }
    }
    
    @Override
    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }
    
    @Override
    public void addMessage(Message message) throws EqualMessageIdException, EqualPersonIdException {
        if (containsMessage(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        } else if (message.getType() == 0) {
            if (message.getPerson1().equals(message.getPerson2())) {
                throw new MyEqualPersonIdException(message.getPerson1().getId());
            }
        }
        messages.put(message.getId(), message);
    }
    
    @Override
    public Message getMessage(int id) {
        if (messages.containsKey(id)) {
            return messages.get(id);
        }
        return null;
    }
    
    @Override
    public void sendMessage(int id) throws RelationNotFoundException,
            MessageIdNotFoundException, PersonIdNotFoundException {
        if (!containsMessage(id)) {
            throw new MyMessageIdNotFoundException(id);
        }
        Message message = getMessage(id);
        Person sender = message.getPerson1();
        if (message.getType() == 0) {
            Person receiver = message.getPerson2();
            if (!sender.isLinked(receiver)) {
                throw new MyRelationNotFoundException(sender.getId(), receiver.getId());
            } else {
                sender.addSocialValue(message.getSocialValue());
                receiver.addSocialValue(message.getSocialValue());
                ((LinkedList<Message>) (receiver.getMessages())).addFirst(message);
                messages.remove(id);
            }
        } else {
            Group group = message.getGroup();
            if (!group.hasPerson(sender)) {
                throw new MyPersonIdNotFoundException(sender.getId());
            } else {
                MyGroup myGroup = (MyGroup) (group);
                myGroup.addSocialValue(message.getSocialValue());
                messages.remove(id);
                for (Person p : myGroup.getPeople()) {
                    p.addSocialValue(message.getSocialValue());
                }
            }
        }
    }
    
    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!people.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        int groupSocialValue = 0;
        MyPerson person = (MyPerson) people.get(id);
        for (Integer idx : person.getAllGroups()) {
            MyGroup group = (MyGroup) groups.get(idx);
            groupSocialValue += group.getSocialValue();
        }
        return people.get(id).getSocialValue();
    }
    
    @Override
    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!people.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return people.get(id).getReceivedMessages();
    }
}
