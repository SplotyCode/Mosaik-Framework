package io.github.splotycode.mosaik.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.PrintWriter;
import java.io.StringWriter;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionUtil {

    public static String toString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter stream = new PrintWriter(sw);
        throwable.printStackTrace(stream);
        return sw.toString();
    }

    public static boolean instanceOfCause(Throwable throwable, Class<? extends Throwable> clazz) {
        return getInstanceOfCause(throwable, clazz) != null;
    }

    public static <T extends Throwable> T getInstanceOfCause(Throwable throwable, Class<T> clazz) {
        while (throwable != null) {
            if (clazz.isAssignableFrom(throwable.getClass())) {
                return (T) throwable;
            }
            throwable = throwable.getCause();
        }
        return null;
    }

    public static Throwable getRootCause(Throwable e) {
        while (true) {
            if (e.getCause() == null) return e;
            e = e.getCause();
        }
    }

    public static RuntimeException toRuntime(Throwable throwable) {
        if (throwable instanceof RuntimeException) {
            return (RuntimeException) throwable;
        }
        return new RuntimeException("Converted to Runtime Exception", throwable);
    }

    public static RuntimeException toRuntime(Throwable throwable, String message) {
        return new RuntimeException(message, throwable);
    }

    public static void throwRuntime(Throwable throwable) {
        throw toRuntime(throwable);
    }

    public static void throwRuntime(Throwable throwable, String message) {
        throw toRuntime(throwable, message);
    }

}
