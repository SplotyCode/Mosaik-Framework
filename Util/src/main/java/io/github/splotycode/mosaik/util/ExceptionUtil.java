package io.github.splotycode.mosaik.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * General Utils for Terminable's
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionUtil {

    /**
     * Converts a throwable to a String.
     * The string should look exactly the same as in the console
     */
    public static String toString(Throwable throwable) {
        StringWriter sw = new StringWriter();
        PrintWriter stream = new PrintWriter(sw);
        throwable.printStackTrace(stream);
        return sw.toString();
    }

    /**
     * Checks if the throwable or its causes are assignable from a class
     */
    public static boolean instanceOfCause(Throwable throwable, Class<? extends Throwable> clazz) {
        return getInstanceOfCause(throwable, clazz) != null;
    }

    /**
     * Get a throwable or its causes are assignable from a class
     * @param throwable the throwable that should be checked
     * @param clazz the class that the found throwable should have
     * @param <T> the type of class
     * @return a throwable of type T if found or else null
     */
    public static <T extends Throwable> T getInstanceOfCause(Throwable throwable, Class<T> clazz) {
        while (throwable != null) {
            if (clazz.isAssignableFrom(throwable.getClass())) {
                return (T) throwable;
            }
            throwable = throwable.getCause();
        }
        return null;
    }

    /**
     * Gets the route cause of a throwable
     */
    public static Throwable getRootCause(Throwable throwable) {
        while (true) {
            if (throwable.getCause() == null) return throwable;
            throwable = throwable.getCause();
        }
    }

    /**
     * Wraps a throwable to RuntimeException.
     * If the throwable already is a RuntimeException it will just return the original throwable.
     */
    public static RuntimeException toRuntime(Throwable throwable) {
        return toRuntime(throwable, "Converted to Runtime Exception");
    }

    /**
     * Wraps a throwable to RuntimeException.
     * If the throwable already is a RuntimeException it will just return the original throwable.
     * @param message the message that the wrapped throwable should have
     */
    public static RuntimeException toRuntime(Throwable throwable, String message) {
        if (throwable instanceof RuntimeException) {
            return (RuntimeException) throwable;
        }
        return new RuntimeException(message, throwable);
    }

    /**
     * Throws a Throwable by using the <code>toRuntime</code> method
     */
    public static void throwRuntime(Throwable throwable) {
        throw toRuntime(throwable);
    }

    /**
     * Throws a Throwable by using the <code>toRuntime</code> method
     * @param message the message that the wrapped throwable should have
     */
    public static void throwRuntime(Throwable throwable, String message) {
        throw toRuntime(throwable, message);
    }

}
