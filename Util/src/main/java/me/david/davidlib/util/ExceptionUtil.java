package me.david.davidlib.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class ExceptionUtil {

    public static String toString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter stream = new PrintWriter(sw);
        throwable.printStackTrace(stream);
        return sw.toString();
    }

    public static boolean instanceOfCause(Throwable throwable, Class<? extends Throwable> clazz) {
        while (throwable != null) {
            if (clazz.isAssignableFrom(throwable.getClass())) {
                return true;
            }
            throwable = throwable.getCause();
        }
        return false;
    }

}
