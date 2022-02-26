import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;

public class Adventurer {
    private final int id;
    private final String name;
    private ArrayList<Equipment> equipments;
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
    
    public void addBottle(Bottle bottle) {
        equipments.add(bottle);
    }
    
    public void addExpBottle(ExpBottle expBottle) {
        equipments.add(expBottle);
    }
    
    public void addHealingPotion(HealingPotion healingPotion) {
        equipments.add(healingPotion);
    }
    
    public void addSword(Sword sword) {
        equipments.add(sword);
    }
    
    public void addEpicSword(EpicSword epicSword) {
        equipments.add(epicSword);
    }
    
    public void addRareSword(RareSword rareSword) {
        equipments.add(rareSword);
    }
    
    public void deleteEquipment(int equId) {
        equipments.removeIf(i -> i.getId() == equId);
    }
    
    public void addEquipment(int equType, Scanner scanner) {
        int equId = scanner.nextInt();
        String name = scanner.next();
        long price = scanner.nextLong();
        double extraAttribute = scanner.nextDouble();
        switch (equType) {
            case 1:
                Bottle bottle = new Bottle(equId, name, price, extraAttribute);
                this.addBottle(bottle);
                break;
            case 2:
                double efficiency = scanner.nextDouble();
                HealingPotion healingPotion = new HealingPotion(equId, name, price, extraAttribute
                        , efficiency);
                this.addHealingPotion(healingPotion);
                break;
            case 3:
                double expRatio = scanner.nextDouble();
                ExpBottle expBottle = new ExpBottle(equId, name, price, extraAttribute, expRatio);
                this.addExpBottle(expBottle);
                break;
            case 4:
                Sword sword = new Sword(equId, name, price, extraAttribute);
                this.addSword(sword);
                break;
            case 5:
                double extraExpBonus = scanner.nextDouble();
                RareSword rareSword = new RareSword(equId, name, price, extraAttribute
                        , extraExpBonus);
                this.addRareSword(rareSword);
                break;
            case 6:
                double evolveRatio = scanner.nextDouble();
                EpicSword epicSword = new EpicSword(equId, name, price, extraAttribute
                        , evolveRatio);
                this.addEpicSword(epicSword);
                break;
            default:
            
        }
    }
    
    public int getId() {
        return id;
    }
    
    public BigInteger sumOfPrice() {
        BigInteger sum = BigInteger.valueOf(0);
        for (Equipment i : equipments) {
            sum = sum.add(BigInteger.valueOf(i.getPrice()));
        }
        return sum;
    }
    
    public long maxPrice() {
        long max = -1;
        for (Equipment i : equipments) {
            long currentPrice = i.getPrice();
            if (currentPrice > max) {
                max = currentPrice;
            }
        }
        return max;
    }
    
    public int total() {
        return this.equipments.size();
    }
    
    public void print(int equId) {
        for (Equipment equipment : equipments) {
            if (equipment.getId() == equId) {
                if (equipment instanceof HealingPotion) {
                    HealingPotion ref = (HealingPotion) equipment;
                    ref.printHealingPotion();
                }
                else if (equipment instanceof ExpBottle) {
                    ExpBottle ref = (ExpBottle) equipment;
                    ref.printExpBottle();
                }
                else if (equipment instanceof Bottle) {
                    Bottle ref = (Bottle) equipment;
                    ref.printBottle();
                }
                else if (equipment instanceof RareSword) {
                    RareSword ref = (RareSword) equipment;
                    ref.printRareSword();
                }
                else if (equipment instanceof EpicSword) {
                    EpicSword ref = (EpicSword) equipment;
                    ref.printEpicSword();
                }
                else {
                    Sword ref = (Sword) equipment;
                    ref.printSword();
                }
            }
        }
    }
    
    public void prentAdventurer() {
        System.out.printf(
                "The adventurer's id is %d, name is %s, health is %s, exp is %s, money is %s.%n"
                , this.getId(), this.name.toString(), Double.toString(health),
                Double.toString(exp), Double.toString(money));
    }
    
    public void useAll() {
        ArrayList<Equipment> sorted = new ArrayList<>(equipments);
        Collections.sort(sorted, Comparator.reverseOrder());
        for (Equipment equipment : sorted) {
            try {
                equipment.use(this);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }
    
}
