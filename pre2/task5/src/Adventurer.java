import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class Adventurer implements ValuableObject {
    private final int id;
    private final String name;
    private ArrayList<ValuableObject> equipments;
    private double health;
    private double money;
    private double exp;
    
    public Adventurer(int id, String name) {
        this.id = id;
        this.name = name;
        this.equipments = new ArrayList<>();
        this.health = 100.0;
        this.exp = 0.0;
        this.money = 0.0;
    }
    
    public String getName() {
        return name;
    }
    
    public void setHealth(double health) {
        this.health = health;
    }
    
    public double getHealth() {
        return health;
    }
    
    public void setExp(double exp) {
        this.exp = exp;
    }
    
    public double getExp() {
        return exp;
    }
    
    public void setMoney(double money) {
        this.money = money;
    }
    
    public double getMoney() {
        return money;
    }
    
    public void deleteEquipment(int equId) {
        equipments.removeIf(i -> i.getId() == equId);
    }
    
    public void addAdventurer(ValuableObject val) {
        this.equipments.add(val);
    }
    
    public void addEquipment(int equType, Scanner scanner) {
        int equId = scanner.nextInt();
        String name = scanner.next();
        BigInteger price = scanner.nextBigInteger();
        double extraAttribute = scanner.nextDouble();
        switch (equType) {
            case 1:
                Bottle bottle = new Bottle(equId, name, price, extraAttribute);
                this.equipments.add(bottle);
                break;
            case 2:
                double efficiency = scanner.nextDouble();
                HealingPotion healingPotion = new HealingPotion(equId, name, price, extraAttribute
                        , efficiency);
                this.equipments.add(healingPotion);
                break;
            case 3:
                double expRatio = scanner.nextDouble();
                ExpBottle expBottle = new ExpBottle(equId, name, price, extraAttribute, expRatio);
                this.equipments.add(expBottle);
                break;
            case 4:
                Sword sword = new Sword(equId, name, price, extraAttribute);
                this.equipments.add(sword);
                break;
            case 5:
                double extraExpBonus = scanner.nextDouble();
                RareSword rareSword = new RareSword(equId, name, price, extraAttribute
                        , extraExpBonus);
                this.equipments.add(rareSword);
                break;
            case 6:
                double evolveRatio = scanner.nextDouble();
                EpicSword epicSword = new EpicSword(equId, name, price, extraAttribute
                        , evolveRatio);
                this.equipments.add(epicSword);
                break;
            default:
            
        }
    }
    
    public int getId() {
        return id;
    }
    
    public BigInteger sumOfPrice() {
        BigInteger sum = BigInteger.valueOf(0);
        for (ValuableObject i : equipments) {
            sum = sum.add(i.getPrice());
        }
        return sum;
    }
    
    public BigInteger maxPrice() {
        BigInteger max = BigInteger.ZERO;
        for (ValuableObject i : equipments) {
            BigInteger currentPrice = i.getPrice();
            if (currentPrice.compareTo(max) > 0) {
                max = currentPrice;
            }
        }
        return max;
    }
    
    public int total() {
        return this.equipments.size();
    }
    
    public void print(int equId) {
        for (ValuableObject equipment : equipments) {
            if (equipment.getId() == equId) {
                if (equipment instanceof HealingPotion) {
                    HealingPotion ref = (HealingPotion) equipment;
                    System.out.println(ref.toString());
                } else if (equipment instanceof ExpBottle) {
                    ExpBottle ref = (ExpBottle) equipment;
                    System.out.println(ref.toString());
                } else if (equipment instanceof Bottle) {
                    Bottle ref = (Bottle) equipment;
                    System.out.println(ref.toString());
                } else if (equipment instanceof RareSword) {
                    RareSword ref = (RareSword) equipment;
                    System.out.println(ref.toString());
                } else if (equipment instanceof EpicSword) {
                    EpicSword ref = (EpicSword) equipment;
                    System.out.println(ref.toString());
                } else {
                    System.out.println(equipment.toString());
                }
            }
        }
    }
    
    public void printAdventurer() {
        System.out.printf(
                "The adventurer's id is %d, name is %s, health is %s, exp is %s, money is %s.%n"
                , this.getId(), this.name.toString(), Double.toString(health),
                Double.toString(exp), Double.toString(money));
    }
    
    public void useAll(Adventurer user) {
        ArrayList<ValuableObject> sorted = new ArrayList<>(equipments);
        Collections.sort(sorted, (x, y) -> {
            // compare the price first
            if (!x.getPrice().equals(y.getPrice())) {
                return -x.getPrice().compareTo(y.getPrice());
            }
            // if the price equals, then compare the id
            if (x.getId() > y.getId()) {
                return -1;
            } else if (x.getId() < y.getId()) {
                return 1;
            }
            return 0;
        });
        for (ValuableObject equipment : sorted) {
            try {
                equipment.use(user);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
    public BigInteger getPrice() {
        return this.sumOfPrice();
    }
    
    public void use(Adventurer user) {
        this.useAll(user);
    }
    
    @Override
    public String toString() {
        return String.format(
                "The adventurer's id is %d, name is %s, health is %s, exp is %s, money is %s."
                , this.getId(), this.name.toString(), Double.toString(health),
                Double.toString(exp), Double.toString(money));
    }
}
