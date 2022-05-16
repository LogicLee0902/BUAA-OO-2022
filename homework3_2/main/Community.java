package main;

import com.oocourse.spec2.main.Person;

public class Community {
    private Community higher;
    private final Person lord;
    private int amount;
    
    Community(Person lord) {
        this.lord = lord;
        this.amount = 1;
        this.higher = null;
    }
    
    public Person getLord() {
        return find().getLord();
    }
    
    public void setHigher(Community higher) {
        this.higher = higher;
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    public Community find() {
        Community tmp = this;
        while (tmp.getHigher() != null) {
            tmp = tmp.getHigher();
        }
        return tmp;
    }

    public Community getHigher() {
        return higher;
    }
    
    public int getAmount() {
        return amount;
    }
    
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Community)) {
            return false;
        }
        Community myCommunity = (Community) other;
        return myCommunity.getLord().equals(this.getLord());
    }
}
