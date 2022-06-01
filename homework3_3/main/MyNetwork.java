package main;

import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;

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
    private final HashSet<Integer> emojiList = new HashSet<Integer>();
    private final HashMap<Integer, Integer> emojiHeatList = new HashMap<Integer, Integer>();
    
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
                if (group.hasPerson(p2)) {
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
    public void addMessage(Message message)
            throws EqualMessageIdException, EqualPersonIdException, EmojiIdNotFoundException {
        if (containsMessage(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        } else {
            if (message instanceof EmojiMessage) {
                if (!containsEmojiId(((EmojiMessage) message).getEmojiId())) {
                    throw new MyEmojiIdNotFoundException((((EmojiMessage) message).getEmojiId()));
                }
            }
            if (message.getType() == 0) {
                if (message.getPerson1().equals(message.getPerson2())) {
                    throw new MyEqualPersonIdException(message.getPerson1().getId());
                }
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
                send(message, sender, receiver, id);
            }
        } else {
            Group group = message.getGroup();
            if (!group.hasPerson(sender)) {
                throw new MyPersonIdNotFoundException(sender.getId());
            } else {
                MyGroup myGroup = (MyGroup) (group);
                int money = 0;
                if (message instanceof RedEnvelopeMessage) {
                    money = ((RedEnvelopeMessage) message).getMoney() / group.getSize();
                    sender.addMoney(-1 * group.getSize() * money);
                }
                for (Person p : myGroup.getPeople()) {
                    p.addSocialValue(message.getSocialValue());
                    dealWithMessage(message, money, p);
                }
                messages.remove(id);
            }
        }
    }
    
    private void send(Message message, Person sender, Person receiver, int id) {
        sender.addSocialValue(message.getSocialValue());
        receiver.addSocialValue(message.getSocialValue());
        ((LinkedList<Message>) (receiver.getMessages())).addFirst(message);
        int money = 0;
        if (message instanceof RedEnvelopeMessage) {
            money = ((RedEnvelopeMessage) message).getMoney();
            sender.addMoney(-1 * money);
        }
        dealWithMessage(message, money, receiver);
        messages.remove(id);
    }
    
    private void dealWithMessage(Message message, int money, Person person) {
        if (message instanceof RedEnvelopeMessage) {
            person.addMoney(money);
        } else if (message instanceof EmojiMessage) {
            if (message.getType() == 1 && !person.equals(message.getPerson1())) {
                return;
            }
            EmojiMessage emoji = (EmojiMessage) message;
            int origin = emojiHeatList.get(emoji.getEmojiId());
            emojiHeatList.put(emoji.getEmojiId(), origin + 1);
        }
    }
    
    @Override
    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!people.containsKey(id)) {
            throw new MyPersonIdNotFoundException(id);
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
    
    @Override
    public boolean containsEmojiId(int id) {
        return emojiList.contains(id);
    }
    
    @Override
    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (containsEmojiId(id)) {
            throw new MyEqualEmojiIdException(id);
        }
        emojiList.add(id);
        emojiHeatList.put(id, 0);
    }
    
    @Override
    public int queryMoney(int id) throws PersonIdNotFoundException {
        MyPerson person = (MyPerson) people.get(id);
        if (person == null) {
            throw new MyPersonIdNotFoundException(id);
        }
        return person.getMoney();
    }
    
    @Override
    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!containsEmojiId(id)) {
            throw new MyEmojiIdNotFoundException(id);
        }
        return emojiHeatList.get(id);
    }
    
    @Override
    public int deleteColdEmoji(int limit) {
        emojiList.removeIf(e -> emojiHeatList.get(e) < limit);
        emojiHeatList.keySet().removeIf(e -> emojiHeatList.get(e) < limit);
        messages.keySet().removeIf(m -> ((messages.get(m) instanceof EmojiMessage)
                && !containsEmojiId(((EmojiMessage) messages.get(m)).getEmojiId())));
        return emojiList.size();
    }
    
    @Override
    public void clearNotices(int personId) throws PersonIdNotFoundException {
        if (!contains(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }
        MyPerson person = (MyPerson) people.get(personId);
        person.getMessages().removeIf(m -> (m instanceof NoticeMessage));
    }
    
    @Override
    public int sendIndirectMessage(int id) throws MessageIdNotFoundException {
        if (!containsMessage(id) || messages.get(id).getType() == 1) {
            throw new MyMessageIdNotFoundException(id);
        }
        MyMessage message = (MyMessage) messages.get(id);
        try {
            if (!isCircle(message.getPerson1().getId(), message.getPerson2().getId())) {
                return -1;
            } else {
                send(message, message.getPerson1(), message.getPerson2(), id);
                PriorityQueue<DisToCur> queue =
                        new PriorityQueue<>(Comparator.comparingInt(DisToCur::getDistance));
                HashMap<Integer, Integer> distance = new HashMap<>();
                HashSet<Integer> visited = new HashSet<Integer>();
                queue.add(new DisToCur(message.getPerson1().getId(), 0));
                distance.put(message.getPerson1().getId(), 0);
                while (queue.size() != 0) {
                    MyPerson person = (MyPerson) people.get(queue.poll().getId());
                    if (person.equals(message.getPerson2())) {
                        return distance.get(person.getId());
                    }
                    visited.add(person.getId());
                    for (Person p : person.getValue().keySet()) {
                        if (!visited.contains(p.getId())) {
                            int newDistance = distance.get(person.getId())
                                    + person.getValue().get(p);
                            if (!distance.containsKey(p.getId())
                                    || newDistance < distance.get(p.getId())) {
                                distance.put(p.getId(), newDistance);
                                queue.add(new DisToCur(p.getId(), newDistance));
                            }
                        }
                    }
                }
            }
        } catch (PersonIdNotFoundException e) {
            throw new RuntimeException(e);
        }
        return -1;
    }
}
