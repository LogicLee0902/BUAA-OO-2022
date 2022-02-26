import java.util.Scanner;

public class Test {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String sentence = scanner.nextLine();
            for (String i : sentence.split(";")) {
                if (i.isEmpty()) {
                    continue;
                }
                int startIndex;
                //System.out.println("String: " + i);
                for (startIndex = 0; startIndex < i.length(); ++startIndex) {
                    if (i.charAt(startIndex) != ' ') {
                        break;
                    }
                }
                //System.out.println("Start from " + startIndex);
                if (startIndex == i.length()) {
                    continue;
                }
                System.out.println(i.substring(startIndex) + ";");
            }
        }
    }
}
