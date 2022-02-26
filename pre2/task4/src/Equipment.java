public class Equipment implements Comparable<Equipment> {
    private int id;
    private String name;
    private long price;
    
    public Equipment() {
    }
    
    public int getId() {
        return id;
    }
    
    public long getPrice() {
        return price;
    }
    
    public String getName() {
        return name;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public void setPrice(long price) {
        this.price = price;
    }
    
    @Override
    public int compareTo(Equipment other) {
        // compare the price first
        if (this.getPrice() > other.getPrice()) {
            return 1;
        } else if (this.getPrice() < other.getPrice()) {
            return -1;
        }
        
        // if the price equals, then compare the id
        if (this.getId() > other.getId()) {
            return 1;
        } else if (this.getId() < other.getId()) {
            return -1;
        }
        
        return 0;
    }
    
    public void use(Adventurer user) throws Exception {
    
    }
}
    

