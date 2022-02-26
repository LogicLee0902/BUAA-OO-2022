import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static final ArrayList<Adventurer> ADVENTURERS = new ArrayList<>();
    
    public static void adventurerUse(int advId) {
        for (Adventurer i : ADVENTURERS) {
            if (i.getId() == advId) {
                i.useAll(i);
                break;
            }
        }
    }
    
    public static void print(int advId) {
        for (Adventurer i : ADVENTURERS) {
            if (i.getId() == advId) {
                i.printAdventurer();
                break;
            }
        }
    }
    
    public static void total(int advId) {
        for (Adventurer i : ADVENTURERS) {
            if (i.getId() == advId) {
                System.out.println(i.total());
                break;
            }
        }
    }
    
    public static void price(int advId) {
        for (Adventurer i : ADVENTURERS) {
            if (i.getId() == advId) {
                System.out.println(i.sumOfPrice());
            }
        }
    }
    
    public static Adventurer findEmployer(int advId) {
        for (Adventurer i : ADVENTURERS) {
            if (i.getId() == advId) {
                return i;
            }
        }
        return null;
    }
    
    public static void findMax(int advId) {
        for (Adventurer i : ADVENTURERS) {
            if (i.getId() == advId) {
                System.out.println(i.maxPrice());
            }
        }
    }
    
    public static void delete(int advId, int equId) {
        for (Adventurer i : ADVENTURERS) {
            if (i.getId() == advId) {
                i.deleteEquipment(equId);
            }
        }
    }
    
    public static void main(String[] argv) {
        Scanner scanner = new Scanner(System.in);
        int m = scanner.nextInt();
        for (int j = 0; j < m; ++j) {
            int type = scanner.nextInt();
            int advId = scanner.nextInt();
            switch (type) {
                case 1:
                    String name = scanner.next();
                    ADVENTURERS.add(new Adventurer(advId, name));
                    break;
                case 2:
                    int equType = scanner.nextInt();
                    for (Adventurer i : ADVENTURERS) {
                        if (i.getId() == advId) {
                            i.addEquipment(equType, scanner);
                        }
                    }
                    break;
                case 3:
                    int equId = scanner.nextInt();
                    delete(advId, equId);
                    break;
                case 4:
                    price(advId);
                    break;
                case 5:
                    findMax(advId);
                    break;
                case 6:
                    total(advId);
                    break;
                case 7:
                    equId = scanner.nextInt();
                    for (Adventurer i : ADVENTURERS) {
                        if (i.getId() == advId) {
                            i.print(equId);
                        }
                    }
                    break;
                case 8:
                    adventurerUse(advId);
                    break;
                case 9:
                    print(advId);
                    break;
                case 10:
                    int hireId = scanner.nextInt();
                    Adventurer employer;
                    employer = findEmployer(advId);
                    //search for the employer
                    for (Adventurer i : ADVENTURERS) {
                        if (i.getId() == hireId) {
                            employer.addAdventurer(i);
                        }
                    }
                    break;
                default:
            }
        }
    }
}
