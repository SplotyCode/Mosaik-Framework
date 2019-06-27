package io.github.splotycode.mosaik.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;

/**
 * General Utils for strings
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class StringUtil {

    /**
     * Checks if a CharSequence is empty or null
     * @param str the string to check
     * @return true is empty or else false
     */
    public static boolean isEmpty(CharSequence str){
        return str == null || str.length() == 0;
    }

    /**
     * Checks if a CharSequence is empty or null or has only whitespaces
     * @param str the string to check
     * @return true is empty or else false
     */
    public static boolean isEmptyDeep(CharSequence str){
        if (str == null) return true;
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (!Character.isWhitespace(ch)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the number of bytes formatted
     * @param bytes the number of bytes
     */
    public static String humanReadableBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        char pre = "kMGTPE".charAt(exp-1);
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }

    /**
     * @deprecated use {@link Character#isWhitespace(char)}
     */
    public static boolean isNoWhiteSpace(char ch) {
        return ch != Character.MIN_VALUE && ch != ' ' && ch != '\n' && ch != '\r' && ch != '\t';
    }

    @Deprecated
    public static boolean isNoSpecialSpace(char ch) {
        return ch != '\n' && ch != '\r' && ch != '\t' && ch != Character.MIN_VALUE;
    }

    /**
     * @deprecated use {@link Character#isWhitespace(char)}
     */
    @Deprecated
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

    /**
     * Checks if a String could be parsed as a float
     * @param str the string to check
     * @return true if float or else false
     */
    public static boolean isFloat(String str){
        if (str == null) return false;
        try {
            Float.valueOf(str);
        } catch (NumberFormatException ex){
            return false;
        }
        return true;
    }

    /**
     * @deprecated use {@link Float#valueOf(String)}
     */
    @Deprecated
    public static float toFloat(String str){
        return Float.valueOf(str);
    }

    /**
     * Checks if a String could be parsed as a double
     * @param str the string to check
     * @return true if double or else false
     */
    public static boolean isDouble(String str) {
        if (str == null) return false;
        try {
            Double.valueOf(str);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    /**
     * @deprecated use {@link Double#valueOf(String)}
     */
    public static double toDouble(String str){
        return Double.parseDouble(str);
    }

    /**
     * Checks if a String could be parsed as a long
     * @param str the string to check
     * @return true if long or else false
     */
    public static boolean isLong(String str) {
        if (str == null) return false;
        try {
            Long.valueOf(str);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    /**
     * @deprecated use {@link Long#valueOf(String)}
     */
    public static long toLong(String str){
        return Long.parseLong(str);
    }

    /**
     * Checks if a String could be parsed as a integer
     * @param str the string to check
     * @return true if integer or else false
     */
    public static boolean isInteger(String str){
        if (str == null) return false;
        try {
            Integer.valueOf(str);
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }

    /**
     * Transform a Object (T) to String.
     * This is used for Join methods that is why it is called Joiner.
     */
    public interface Joiner<T> {

        String join(T obj);

    }

    /**
     * This Joiner instance converts String to Strings by doing nothing
     */
    public static final Joiner<String> STRING_JOINER = str -> str;

    /**
     * Combines multiple strings with a comma
     * @param iterable the strings that should be combined
     * @return the combined string
     */
    public static String join(Iterable<String> iterable) {
        return join(iterable, ", ");
    }

    /**
     * Combines multiple strings
     * @param iterable the strings that should be combined
     * @param separator separator the strings
     * @return the combined string
     */
    public static String join(Iterable<String> iterable, String separator) {
        return join(iterable, STRING_JOINER, separator);
    }

    /**
     * Combines multiple strings
     * @param array the strings that should be combined
     * @param separator separator the strings
     * @return the combined string
     */
    public static String join(String[] array, String separator){
        return join(array, STRING_JOINER, separator);
    }

    /**
     * Combines multiple strings with a comma
     * @param array the strings that should be combined
     * @return the combined string
     */
    public static String join(String[] array){
        return join(array, STRING_JOINER, ", ");
    }

    /**
     * Combines multiple objects with a specific Joiner.
     * The string will be separated by a comma
     * @param iterable the strings that should be combined
     * @return the combined string
     */
    public static <T> String join(Iterable<T> iterable, Joiner<T> joiner) {
        return join(iterable, joiner, ", ");
    }

    /**
     * Combines multiple objects with a specific Joiner.
     * @param iterable the strings that should be combined
     * @param separator separator the strings
     * @return the combined string
     */
    public static <T> String join(Iterable<T> iterable, Joiner<T> joiner, String separator) {
        if (iterable == null || separator == null) return null;
        if (joiner == null) throw new IllegalArgumentException("joiner");
        StringBuilder builder = new StringBuilder();
        for (T element : iterable)
            builder.append(joiner.join(element)).append(separator);
        String result = builder.toString();
        if(result.endsWith(separator)) return result.substring(0, result.length()-separator.length());
        return result;
    }

    /**
     * Combines multiple objects with a specific Joiner.
     * The string will be separated by a comma
     * @param array the strings that should be combined
     * @return the combined string
     */
    public static <T> String join(T[] array, Joiner<T> joiner) {
        return join(array, joiner, ", ");
    }

    /**
     * Combines multiple objects with a specific Joiner.
     * @param array the strings that should be combined
     * @param separator separator the strings
     * @return the combined string
     */
    public static <T> String join(T[] array, Joiner<T> joiner, String separator) {
        if (array == null || separator == null) return null;
        if (joiner == null) throw new IllegalArgumentException("joiner");
        StringBuilder builder = new StringBuilder();
        for (T element : array)
            builder.append(joiner.join(element)).append(separator);
        String result = builder.toString();
        if(result.endsWith(separator)) return result.substring(0, result.length()-separator.length());
        return result;
    }

    /**
     * Inserts objects into a String
     * @param string the raw string
     * @param args the objects you want to insert
     * @return the formatted string
     */
    public static String format(String string, Object... args) {
        if (string == null) return null;
        return new MessageFormat(string).format(args);
    }

    /**
     * Checks if two cars equals ignoring the case.
     */
    public static boolean charsEqualIgnoreCase(char a, char b) {
        return a == b || Character.toLowerCase(a) == Character.toLowerCase(b);
    }

    /**
     * Checks if two cars equals ignoring the case.
     */
    public static boolean endsWithChar(String str, char suffix) {
        return !isEmpty(str) && str.charAt(str.length() - 1) == suffix;
    }

    /**
     * Checks if a string starts with a string ignoring the case
     * @param str the string that should be checked
     * @param prefix the string that <code>str</code> starts with
     * @return true if it matches or else false
     */
    public static boolean startsWithIgnoreCase(String str, String prefix) {
        if (str == null || prefix == null) return false;
        int stringLength = str.length();
        int prefixLength = prefix.length();
        return stringLength >= prefixLength && str.regionMatches(true, 0, prefix, 0, prefixLength);
    }

    /**
     * Checks if a string ends with a string ignoring the case
     * @param text the string that should be checked
     * @param suffix the string that <code>str</code> ends with
     * @return true if it matches or else false
     */
    public static boolean endsWithIgnoreCase(String text, String suffix) {
        if (text == null || suffix ==  null) return false;
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

    /**
     * Get the index of a char in String.
     * The range of this check can be specified.
     * It will try to find the last index.
     * @param str the string that should be checked
     * @param c the char we want to find
     * @param start at what position should we start the string
     * @param end at what position should we end the string
     * @return the last index of the char, -1 if the char is not present in string
     */
    public static int lastIndexOf(String str, char c, int start, int end) {
        if (str == null) return -1;
        start = Math.max(start, 0);
        for (int i = Math.min(end, str.length()) - 1; i >= start; i--) {
            if (str.charAt(i) == c) return i;
        }
        return -1;
    }

    /**
     * Checks if a string contains another string.
     * This check will ignore the case.
     * @param where the source
     * @param what the string you want to find in <code>where</code>
     * @return true when the check is successful or else false
     */
    public static boolean containsIgnoreCase(String where, String what) {
        return indexOfIgnoreCase(where, what, 0) >= 0;
    }

    /**
     * Get the index of string in a string.
     * This check will ignore the case
     * The range of this check can be specified.
     * @param what the string that should be checked
     * @param where the source
     * @param what the string you want to find the index of in <code>where</code>
     * @param fromIndex tarting point
     * @return the index of the string,  -1 if the char is not present in string
     */
    public static int indexOfIgnoreCase(String where, String what, int fromIndex) {
        if (where == null || what == null) return -1;
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

    /**
     * Removes the end of a string
     * @param str the string from what you want to remove the end
     * @param places the amount of chars you want to remove
     * @return the new string
     */
    public static String removeLast(String str, int places) {
        if (isEmpty(str)) return str;
        return str.substring(0, str.length() - places);
    }

    /**
     * Checks if a letter is thwo times in string
     * @param str source
     * @param letter the letter you want to check for
     * @return true if the lette is two times in the string or else false
     */
    public static boolean containsDouble(String str, char letter) {
        if (str == null) return false;
        int firstIndex = str.indexOf(letter);
        return firstIndex > -1 && str.indexOf(letter, firstIndex + 1) > -1;
    }

    /**
     * Converts a string into camelcase
     */
    public static String camelCase(String s) {
        return camelCase(s, " ");
    }


    /**
     * Converts a string into camelcase
     * @param separator the separator that should be used.
     *                  For example: My-Name-Is-David or My Name Is David
     */
    public static String camelCase(String str, String separator) {
        return camelCase(str, separator, true);
    }

    /**
     * Converts a string into camelcase
     * @param separator the separator that should be used.
     *                  For example: My-Name-Is-David or My Name Is David
     */
    public static String camelCase(String str, String separator, boolean firstUpper) {
        if (str == null)  return null;

        StringBuilder b = new StringBuilder();
        String[] split = str.split(separator);
        int i = 0;
        for (String srt : split) {
            i++;
            if (!srt.isEmpty()) {
                String first = srt.substring(0, 1);
                if (!firstUpper && i == 1) {
                    first = first.toLowerCase();
                } else {
                    first = first.toUpperCase();
                }
                b.append(first).append(srt.substring(1).toLowerCase());
                if (split.length != i) {
                    b.append(separator);
                }
            }
        }
        return b.toString().trim();
    }

    /**
     * Returns the last split index of a string
     * Example:
     * getLastSplit("hallo my name is david", " ") == "david"
     *
     * @param str the source
     */
    public static String getLastSplit(String str, String separator) {
        if (str == null || separator == null) return null;
        return str.substring(str.lastIndexOf(separator) + 1);
    }

}
