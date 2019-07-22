package io.github.splotycode.mosaik.util.time;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TimeUtil {

    public static long currentOffset(long timestamp) {
        return System.currentTimeMillis() - timestamp;
    }

    public static String formatDelayTimestamp(long timestap) {
        return formatDelay(currentOffset(timestap));
    }

    public static String formatDelay(long delay) {
        delay = Math.max(1, delay / 1000);

        int w = (int) (delay / 604800);
        int d = (int) (delay % 604800 / 86400);
        int h = (int) (delay % 86400 / 3600);
        int m = (int) (delay % 3600 / 60);
        int s = (int) (delay % 60);

        StringBuilder number = new StringBuilder();

        if (w > 0)
            number.append(w).append("w ");
        if (d > 0)
            number.append(d).append("d ");
        if (h > 0)
            number.append(h).append("h ");
        if (m > 0)
            number.append(m).append("m ");
        if (number.length() == 0 || s > 0)
            number.append(s).append("s ");
        return number.substring(0, number.length() - 1);
    }

    /**
     * Checks is a year is a leap year
     * @param year the year as an number (for example 2004)
     * @return true if the year is an leap year or else false
     */
    public static boolean isLeapYear(int year) {
        if (year % 4 != 0) {
            return false;
        } else if (year % 400 == 0) {
            return true;
        }
        return year % 100 != 0;
    }

}
