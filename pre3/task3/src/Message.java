import java.util.ArrayList;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {
    
    private String dialog;
    private ArrayList<Integer> date = new ArrayList<Integer>();
    private static final String DATE_PATTERN = "(\\d+)/(\\d+)/(\\d+)";
    private static final String RECEIVER_PATTERN = "(@.+?\\s)";
    private static final String SENDER_PATTERN = "(-.+?[@:])";
    private String receiver;
    private String sender;
    private final Pattern datePattern = Pattern.compile(DATE_PATTERN);
    private final Pattern receiverPattern = Pattern.compile(RECEIVER_PATTERN);
    private final Pattern senderPattern = Pattern.compile(SENDER_PATTERN);
    
    public Message(String dialog) {
        this.dialog = dialog;
        this.date = setDate(dialog);
        this.receiver = setReceiver(dialog);
        this.sender = setSender(dialog);
        
    }
    
    public String setReceiver(String str) {
        Matcher matcher = receiverPattern.matcher(str);
        if (!matcher.find()) {
            return "";
        }
        int length = matcher.group(0).length();
        return matcher.group(0).substring(1, length - 1);
    }
    
    public String getDialog() {
        return dialog;
    }
    
    public boolean receiverEqual(String name) {
        return this.receiver.equals(name);
    }
    
    public ArrayList setDate(String str) {
        Matcher matcher = datePattern.matcher(str);
        matcher.find();
        ArrayList<Integer> convert = new ArrayList<Integer>();
        int i;
        for (i = 1; i < 4; ++i) {
            convert.add(Integer.parseInt(matcher.group(i)));
        }
        return convert;
    }
    
    public boolean dateEqual(ArrayList<Integer> stdDate) {
        for (int i = 0; i < stdDate.size(); ++i) {
            if (!Objects.equals(date.get(i), stdDate.get(i)) && stdDate.get(i) != -1) {
                return false;
            }
        }
        return true;
    }
    
    public String setSender(String str) {
        Matcher matcher = senderPattern.matcher(str);
        matcher.find();
        int length = matcher.group(0).length();
        return matcher.group(0).substring(1, length - 1);
    }
    
    public boolean senderEqual(String std) {
        return this.sender.equals(std);
    }
}
