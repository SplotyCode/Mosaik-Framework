package me.david.davidlib.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class StringUtil {

    public static boolean isEmpty(String str){
        return str == null || str.length() == 0;
    }

    public static String humanReadableBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        char pre = ("kMGTPE").charAt(exp-1);
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }

    public static boolean isNoWhiteSpace(char ch){
        return ch != Character.MIN_VALUE && ch != ' ' && ch != '\n' && ch != '\r' && ch != '\t';
    }

    public static boolean isNoSpecialSpace(char ch){
        return ch != '\n' && ch != '\r' && ch != '\t' && ch != Character.MIN_VALUE;
    }

    public static boolean isWhiteSpace(char ch){
        return !isNoWhiteSpace(ch);
    }

    public static String fromException(Throwable throwable){
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }

    public static boolean isFloat(String str){
        try {
            Float.valueOf(str);
        } catch (NumberFormatException ex){
            return false;
        }
        return true;
    }

    public static float toFloat(String str){
        return Float.valueOf(str);
    }

    public static boolean isDouble(String str){
        try {
            Double.valueOf(str);
        } catch (NumberFormatException ex){
            return false;
        }
        return true;
    }

    public static double toDouble(String str){
        return Double.parseDouble(str);
    }

    public static boolean isLong(String str){
        try {
            Long.valueOf(str);
        } catch (NumberFormatException ex){
            return false;
        }
        return true;
    }

    public static boolean isInteger(String str){
        try {
            Integer.valueOf(str);
        } catch (NumberFormatException ex){
            return false;
        }
        return true;
    }

    public static long toLong(String str){
        return Long.parseLong(str);
    }

}
