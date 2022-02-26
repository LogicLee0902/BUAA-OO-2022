import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    
    public static void query(String str, ArrayList<Message> messages) {
        String[] instru = str.split(" ");
        switch (instru[0]) {
            case "qdate":
                String[] tmp = instru[1].split("/");
                int[] date = new int[3];
                for (int i = 0; i < tmp.length; ++i) {
                    date[i] = Integer.parseInt(tmp[i]);
                }
                for (Message msg : messages) {
                    if (msg.dateEqual(date)) {
                        System.out.println(msg.getDialog());
                    }
                }
                break;
            case "qsend":
                for (Message msg : messages) {
                    if (msg.senderEqual(instru[1])) {
                        System.out.println(msg.getDialog());
                    }
                }
                break;
            case "qrecv":
                for (Message msg : messages) {
                    if (msg.receiverEqual(instru[1])) {
                        System.out.println(msg.getDialog());
                    }
                }
                break;
            default:
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
            query(sentence, messages);
        }
    }
}
