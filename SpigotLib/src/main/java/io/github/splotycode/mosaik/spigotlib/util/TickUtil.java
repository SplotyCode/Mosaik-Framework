package io.github.splotycode.mosaik.spigotlib.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class TickUtil {

    public static final int TICKS_AT_MIDNIGHT = 18000;
    public static final int TICKS_PER_DAY = 24000;
    public static final int TICKS_PER_HOUR = 1000;
    public static final double TICKS_PER_MINUTE = 1000d / 60d;
    public static final double TICKS_PER_SECOND = 1000d / 60d / 60d;

    public static long hoursMinutesToTicks(final int hours, final int minutes) {
        long ret = TICKS_AT_MIDNIGHT;
        ret += (hours) * TICKS_PER_HOUR;

        ret += (minutes / 60.0) * TICKS_PER_HOUR;

        ret %= TICKS_PER_DAY;
        return ret;
    }

    public static Date ticksToDate(long ticks) {
        ticks = ticks - TICKS_AT_MIDNIGHT + TICKS_PER_DAY;

        final long days = ticks / TICKS_PER_DAY;
        ticks -= days * TICKS_PER_DAY;

        final long hours = ticks / TICKS_PER_HOUR;
        ticks -= hours * TICKS_PER_HOUR;

        final long minutes = (long) Math.floor(ticks / TICKS_PER_MINUTE);
        final double dticks = ticks - minutes * TICKS_PER_MINUTE;

        final long seconds = (long) Math.floor(dticks / TICKS_PER_SECOND);

        final Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.ENGLISH);
        cal.setLenient(true);

        cal.set(0, Calendar.JANUARY, 1, 0, 0, 0);
        cal.add(Calendar.DAY_OF_YEAR, (int) days);
        cal.add(Calendar.HOUR_OF_DAY, (int) hours);
        cal.add(Calendar.MINUTE, (int) minutes);
        cal.add(Calendar.SECOND, (int) seconds + 1);

        return cal.getTime();
    }
}
