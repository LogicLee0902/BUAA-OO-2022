import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void match(String str, ArrayList<Message> messages) {
        String[] instructions = str.split(" ");
        if (instructions[1].equals("A")) {
            for (Message msg : messages) {
                if (msg.getMatchA(Integer.parseInt(instructions[2]))) {
                    System.out.println(msg.getDialog());
                }
            }
        } else if (instructions[1].equals("B")) {
            for (Message msg : messages) {
                if (msg.getMatchB(Integer.parseInt(instructions[2]))) {
                    System.out.println(msg.getDialog());
                }
            }
        }
    }
    
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ArrayList<Message> messages = new ArrayList<>();
        boolean flag = true;
        String sentence;
        while (scanner.hasNext()) {
            while (flag) {
                sentence = scanner.nextLine();
                if (sentence.equals("END_OF_MESSAGE")) {
                    flag = false;
                    break;
                }
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
                    messages.add(new Message((i.substring(startIndex) + ";")));
                }
            }
            sentence = scanner.nextLine();
            match(sentence, messages);
        }
    }
}
