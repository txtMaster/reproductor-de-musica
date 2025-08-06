package utils;

public class FormatUtils {
    static public String msToString(long ms){
        long seconds = ms / 1000;
        long mins = seconds / 60;
        long secs = seconds % 60;
        return String.format("%02d:%02d", mins, secs);
    }
}
