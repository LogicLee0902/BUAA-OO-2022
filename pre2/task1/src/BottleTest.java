import java.util.Scanner;

public class BottleTest {
    public static void main(String[] argv) {
        String name;
        int id;
        long price;
        //declare the attributes
        Scanner scanner = new Scanner(System.in);
        id = scanner.nextInt();
        name = scanner.next();
        price = scanner.nextLong();
        double capacity;
        capacity = scanner.nextDouble();
        //get the input data
        int m;
        Bottle bottle = new Bottle(id, name, price, capacity);
        //initialize the bottle
        m = scanner.nextInt();
        for (int i  = 0; i < m; ++i) {
            int code;
            code = scanner.nextInt();
            switch (code) {
                case 1:
                    System.out.println(bottle.getName());
                    break;
                case 2:
                    System.out.println(bottle.getPrice());
                    break;
                case 3:
                    System.out.println(bottle.getCapacity());
                    break;
                case 4:
                    System.out.println(bottle.isFilled());
                    break;
                case 5:
                    long priceNew;
                    priceNew = scanner.nextLong();
                    bottle.setPrice(priceNew);
                    break;
                case 6:
                    boolean filledNew;
                    filledNew = scanner.nextBoolean();
                    bottle.setFilled(filledNew);
                    break;
                case 7:
                    bottle.returnInformain();
                    break;
                default:
            }
        }
    }
}
