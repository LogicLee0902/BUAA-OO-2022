import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Message {
    private String dialog;
    private static final String A_PATTERN_PRE = "(\"a{2,3}?b{2,4}?c{2,4}?)";
    private static final String A_PATTERN_SUF = "\".*a{2,3}?b{2,4}?c{2,4}?\"";
    private static final String A_PATTERN_SUB = "\".*a{2,3}?b{2,4}?c{2,4}?";
    private static final String A_PATTERN_IN = "\".*(a.*?){2,3}(b.*?){2,4}(c.*?){2,4}";
    // re equation for A
    private static final String B_PATTERN_PRE = "(\"a{2,3}?b{2,1000000}?c{2,4}?)";
    private static final String B_PATTERN_SUF = "\".*a{2,3}?b{2,1000000}?c{2,4}?\"";
    private static final String B_PATTERN_SUB = "\".*a{2,3}?b{2,1000000}?c{2,4}?";
    private static final String B_PATTERN_IN = "\".*(a.*?){2,3}(b.*?){2,1000000}(c.*?){2,4}";
    // re equation for B
    private final Pattern patternPreA = Pattern.compile(A_PATTERN_PRE);
    private final Pattern patternSufA = Pattern.compile(A_PATTERN_SUF);
    private final Pattern patternSubA = Pattern.compile(A_PATTERN_SUB);
    private final Pattern patternInA = Pattern.compile(A_PATTERN_IN);
    // transfer A
    private final Pattern patternPreB = Pattern.compile(B_PATTERN_PRE);
    private final Pattern patternSufB = Pattern.compile(B_PATTERN_SUF);
    private final Pattern patternSubB = Pattern.compile(B_PATTERN_SUB);
    private final Pattern patternInB = Pattern.compile(B_PATTERN_IN);
    // transfer B
    private boolean[] matchA = new boolean[5];
    private boolean[] matchB = new boolean[5];
    
    public Message(String dialog) {
        this.dialog = dialog;
        this.matchA = setMatchA(dialog);
        this.matchB = setMatchB(dialog);
    }
    
    public boolean[] setMatchA(String dialog) {
        boolean[] match = new boolean[5];
        match[1] = checkPreA(dialog);
        match[2] = checkSufA(dialog);
        match[3] = checkSubA(dialog);
        match[4] = checkInA(dialog);
        return match;
    }
    
    public boolean[] setMatchB(String dialog) {
        boolean[] match = new boolean[5];
        match[1] = checkPreB(dialog);
        match[2] = checkSufB(dialog);
        match[3] = checkSubB(dialog);
        match[4] = checkInB(dialog);
        return match;
    }
    
    public boolean checkPreA(String dialog) {
        Matcher matcher = patternPreA.matcher(dialog);
        return matcher.find();
    }
    
    public boolean checkSufA(String dialog) {
        Matcher matcher = patternSufA.matcher(dialog);
        return matcher.find();
    }
    
    public boolean checkSubA(String dialog) {
        Matcher matcher = patternSubA.matcher(dialog);
        return matcher.find();
    }
    
    public boolean checkInA(String dialog) {
        Matcher matcher = patternInA.matcher(dialog);
        return matcher.find();
    }
    
    public boolean checkPreB(String dialog) {
        Matcher matcher = patternPreB.matcher(dialog);
        return matcher.find();
    }
    
    public boolean checkSufB(String dialog) {
        Matcher matcher = patternSufB.matcher(dialog);
        return matcher.find();
    }
    
    public boolean checkSubB(String dialog) {
        Matcher matcher = patternSubB.matcher(dialog);
        return matcher.find();
    }
    
    public boolean checkInB(String dialog) {
        Matcher matcher = patternInB.matcher(dialog);
        return matcher.find();
    }
    
    public boolean getMatchA(int idx) {
        return matchA[idx];
    }
    
    public boolean getMatchB(int idx) {
        return matchB[idx];
    }
    
    public String getDialog() {
        return dialog;
    }
}
