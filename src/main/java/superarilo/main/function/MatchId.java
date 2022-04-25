package superarilo.main.function;

import java.util.regex.Pattern;

public class MatchId {

    private static final String symbol = "^[A-Za-z]+$";
    private static final String regEx = "^[\\u4E00-\\u9FA5A-Za-z0-9]+$";
    public static boolean isEnglish(String str) {
        return Pattern.compile(symbol).matcher(str).matches();
    }
    public static boolean isChineseEnglishNumber(String str) {
        return Pattern.compile(regEx).matcher(str).matches();
    }

}
