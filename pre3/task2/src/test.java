import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class test {
    private final static String DATE_PATTEN = "(@.+\\s)";
    private final static String STRING = "2021/5/3-Mike:\"he@buaaer is unhappy\";";
    public static void main(String[] args) {
        Pattern pattern = Pattern.compile(DATE_PATTEN);
        Matcher matcher = pattern.matcher(STRING);
        matcher.find();
        for (int i = 0; i < matcher.groupCount(); ++i)
            System.out.println((matcher.group(i)));
    }
}
