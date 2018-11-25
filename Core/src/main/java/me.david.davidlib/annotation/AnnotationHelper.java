package me.david.davidlib.annotation;

import me.david.davidlib.annotation.priority.First;
import me.david.davidlib.annotation.priority.Last;
import me.david.davidlib.annotation.priority.Priority;

import java.lang.annotation.Annotation;

public final class AnnotationHelper {

    public static int getPriority(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Last) return Integer.MIN_VALUE;
            if (annotation instanceof First) return Integer.MAX_VALUE;
            if (annotation instanceof Priority) return ((Priority) annotation).priority();
        }
        return 0;
    }

    public static int getPriority(Annotation annotation) {
        if (annotation instanceof Last) return Integer.MIN_VALUE;
        if (annotation instanceof First) return Integer.MAX_VALUE;

        try {
            return ((Priority) annotation).priority();
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("Invalid annotation type", ex);
        }
    }

}
