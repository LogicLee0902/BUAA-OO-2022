import java.util.ArrayList;
import java.util.Scanner;

public class AdventureTest {
    public static void main(String[] argv) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Adventure> adventurers = new ArrayList<>();
        int m = scanner.nextInt();
        for (int i = 0; i < m; ++i) {
            int code;
            int advId;
            code = scanner.nextInt();
            advId = scanner.nextInt();
            switch (code) {
                case 1:
                    String name;
                    name = scanner.next();
                    Adventure adventurer = new Adventure(advId, name);
                    //create an adventurer
                    adventurers.add(adventurer);
                    //add to the ArrayList
                    break;
                case 2:
                    int botId = scanner.nextInt();
                    String botName = scanner.next();
                    long price = scanner.nextLong();
                    double capacity;
                    capacity = scanner.nextDouble();
                    //get the input data
                    Bottle bottle = new Bottle(botId, botName, price, capacity);
                    for (Adventure person : adventurers) {
                        if (person.getId() == advId) {
                            person.addBottle(bottle);
                            //find the target and add the bottle
                            break;
                        }
                    }
                    break;
                case 3:
                    botId = scanner.nextInt();
                    for (Adventure person : adventurers) {
                        if (person.getId() == advId) {
                            person.deleteBottle(botId);
                            break;
                        }
                    }
                    break;
                case 4:
                    for (Adventure person : adventurers) {
                        if (person.getId() == advId) {
                            System.out.println(person.sumOfPrice());
                            break;
                        }
                    }
                    break;
                case 5:
                    for (Adventure person : adventurers) {
                        if (person.getId() == advId) {
                            System.out.println(person.maxPrice());
                            break;
                        }
                    }
                    break;
                default:
            }
        }
    }
}
