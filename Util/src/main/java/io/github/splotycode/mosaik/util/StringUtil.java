package io.github.splotycode.mosaik.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;

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

    public static boolean isNoWhiteSpace(char ch) {
        return ch != Character.MIN_VALUE && ch != ' ' && ch != '\n' && ch != '\r' && ch != '\t';
    }

    public static boolean isNoSpecialSpace(char ch) {
        return ch != '\n' && ch != '\r' && ch != '\t' && ch != Character.MIN_VALUE;
    }

    public static boolean isWhiteSpace(char ch){
        return !isNoWhiteSpace(ch);
    }

    /**
     * @deprecated instead use  {@link ExceptionUtil#toString(Throwable)}
     */
    @Deprecated
    public static String fromException(Throwable throwable) {
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

    public interface Joiner<T> {
        String join(T obj);
    }

    public static final Joiner<String> STRING_JOINER = str -> str;

    public static String join(Iterable<String> iterable) {
        return join(iterable, ", ");
    }

    public static String join(Iterable<String> iterable, String seperator) {
        return join(iterable, STRING_JOINER, seperator);
    }

    public static String join(String[] array, String seperator){
        return join(array, STRING_JOINER, seperator);
    }

    public static String join(String[] array){
        return join(array, STRING_JOINER, ", ");
    }

    public static <T> String join(Iterable<T> iterable, Joiner<T> joiner) {
        return join(iterable, joiner, ", ");
    }

    public static <T> String join(Iterable<T> iterable, Joiner<T> joiner, String seperator) {
        StringBuilder builder = new StringBuilder();
        for (T element : iterable)
            builder.append(joiner.join(element)).append(seperator);
        String result = builder.toString();
        if(result.endsWith(seperator)) return result.substring(0, result.length()-seperator.length());
        return result;
    }

    public static String format(String s, Object... args) {
        return new MessageFormat(s).format(args);
    }

    public static <T> String join(T[] array, Joiner<T> joiner) {
        return join(array, joiner, ", ");
    }

    public static <T> String join(T[] array, Joiner<T> joiner, String seperator) {
        StringBuilder builder = new StringBuilder();
        for (T element : array)
            builder.append(joiner.join(element)).append(seperator);
        String result = builder.toString();
        if(result.endsWith(seperator)) return result.substring(0, result.length()-seperator.length());
        return result;
    }

    public static boolean charsEqualIgnoreCase(char a, char b) {
        return a == b || Character.toLowerCase(a) == Character.toLowerCase(b);
    }

    public static boolean endsWithChar(String str, char suffix) {
        return str != null && str.length() != 0 && str.charAt(str.length() - 1) == suffix;
    }

    public static boolean startsWithIgnoreCase(String str, String prefix) {
        int stringLength = str.length();
        int prefixLength = prefix.length();
        return stringLength >= prefixLength && str.regionMatches(true, 0, prefix, 0, prefixLength);
    }

    public static boolean endsWithIgnoreCase(String text, String suffix) {
        int l1 = text.length();
        int l2 = suffix.length();
        if (l1 < l2) return false;

        for (int i = l1 - 1; i >= l1 - l2; i--) {
            if (!charsEqualIgnoreCase(text.charAt(i), suffix.charAt(i + l2 - l1))) {
                return false;
            }
        }

        return true;
    }

    public static int lastIndexOf(String str, char c, int start, int end) {
        start = Math.max(start, 0);
        for (int i = Math.min(end, str.length()) - 1; i >= start; i--) {
            if (str.charAt(i) == c) return i;
        }
        return -1;
    }

    public static boolean containsIgnoreCase(String where, String what) {
        return indexOfIgnoreCase(where, what, 0) >= 0;
    }

    public static int indexOfIgnoreCase(String where, String what, int fromIndex) {
        int targetCount = what.length();
        int sourceCount = where.length();

        if (fromIndex >= sourceCount) {
            return targetCount == 0 ? sourceCount : -1;
        }

        if (fromIndex < 0) {
            fromIndex = 0;
        }

        if (targetCount == 0) {
            return fromIndex;
        }

        char first = what.charAt(0);
        int max = sourceCount - targetCount;

        for (int i = fromIndex; i <= max; i++) {
            if (!charsEqualIgnoreCase(where.charAt(i), first)) {
                while (++i <= max && !charsEqualIgnoreCase(where.charAt(i), first)) ;
            }
            if (i <= max) {
                int j = i + 1;
                int end = j + targetCount - 1;
                for (int k = 1; j < end && charsEqualIgnoreCase(where.charAt(j), what.charAt(k)); j++, k++) ;

                if (j == end) {
                    return i;
                }
            }
        }

        return -1;
    }

    public static String removeLast(String str, int places) {
        if (isEmpty(str)) return str;
        return str.substring(0, str.length() - places);
    }

    public static boolean containsDouble(String s, char letter) {
        int firstIndex = s.indexOf(letter);

        return firstIndex > -1 && s.indexOf(letter, firstIndex + 1) > -1;
    }

    public static String camelCase(String s) {
        if (s == null)  return null;

        StringBuilder b = new StringBuilder();
        String[] split = s.split(" ");
        for (String srt : split) {
            if (srt.length() > 0) {
                b.append(srt.substring(0, 1).toUpperCase()).append(srt.substring(1).toLowerCase()).append(" ");
            }
        }
        return b.toString().trim();
    }

}
