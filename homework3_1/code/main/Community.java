package main;

import com.oocourse.spec1.main.Person;

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
        if (this.higher == null) {
            return this.lord;
        } else {
            return this.higher.getLord();
        }
    }
    
    public void setHigher(Community higher) {
        this.higher = higher;
    }
    
    public void setAmount(int amount) {
        this.amount = amount;
    }
    
    public Community getHighest() {
        if (this.higher == null) {
            return this;
        } else {
            return this.higher.getHighest();
        }
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
