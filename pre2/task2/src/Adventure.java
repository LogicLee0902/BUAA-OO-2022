import java.math.BigInteger;
import java.util.ArrayList;

public class Adventure {
    private int id;
    private String name;
    private ArrayList<Bottle> bottles;
    
    public Adventure(int id, String name) {
        this.id = id;
        this.name = name;
        this.bottles = new ArrayList<>();
    }
    
    public void addBottle(Bottle bottle) {
        bottles.add(bottle);
    }
    
    public void deleteBottle(int botId) {
        bottles.removeIf(i -> i.getId() == botId);
    }
    
    public int getId() {
        return id;
    }
    
    public BigInteger sumOfPrice() {
        BigInteger sum = BigInteger.valueOf(0);
        for (Bottle i : bottles) {
            BigInteger tmp = sum.add(BigInteger.valueOf(i.getPrice()));
            sum = tmp;
        }
        return sum;
    }
    
    public long maxPrice() {
        long max = -1;
        for (Bottle i : bottles) {
            long currentPrice = i.getPrice();
            if (currentPrice > max) {
                max = currentPrice;
            }
        }
        return max;
    }
}
