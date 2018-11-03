package me.david.davidlib.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public final class ExceptionUtil {

    public static String toString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter stream = new PrintWriter(sw);
        throwable.printStackTrace(stream);
        return sw.toString();
    }

}
