public class Equipment {
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
}
