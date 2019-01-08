package me.david.davidlib.annotations;

import me.david.davidlib.annotations.priority.First;
import me.david.davidlib.annotations.priority.Last;
import me.david.davidlib.annotations.priority.Priority;

import java.lang.annotation.Annotation;

/**
 * Helper Tools for DavidLib Annotations
 */
public final class AnnotationHelper {

    /**
     * Gets the priority of class
     * Not all Annotations must be Priority(Last, First, Priority) Annotations
     * If it founds a Priority Annotation it will not scan the other annotations
     * @param annotations the annotations of the class
     * @return the priority of the annotations
     * @see Last
     * @see First
     * @see Priority
     */
    public static int getPriority(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            if (annotation instanceof Last) return Integer.MIN_VALUE;
            if (annotation instanceof First) return Integer.MAX_VALUE;
            if (annotation instanceof Priority) return ((Priority) annotation).priority();
        }
        return 0;
    }

    /**
     * Gets the priority of Annotation
     * The Annotation must be a Priority(Last, First, Priority) Annotation
     * @param annotation the annotation of the class
     * @return the priority of the annotation
     * @throws IllegalArgumentException when the Annotation could not be correctly interpreted
     * @see Last
     * @see First
     * @see Priority
     */
    public static int getPriority(Annotation annotation) {
        if (annotation instanceof Last) return Integer.MIN_VALUE;
        if (annotation instanceof First) return Integer.MAX_VALUE;

        try {
            return ((Priority) annotation).priority();
        } catch (ClassCastException ex) {
            throw new IllegalArgumentException("Invalid annotation type", ex);
        }
    }

    /**
     * Gets the priority of Object based on its annotations
     * Not all Annotations must be Priority(Last, First, Priority) Annotations
     * If it founds a Priority Annotation it will not scan the other annotations
     * @param object the object of what you want to get the priority
     * @return the priority of the annotations
     * @see Last
     * @see First
     * @see Priority
     */
    public static int getPriority(Object object) {
        return getPriority(object.getClass().getAnnotations());
    }

}
