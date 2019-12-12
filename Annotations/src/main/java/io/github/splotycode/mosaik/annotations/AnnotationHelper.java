package io.github.splotycode.mosaik.annotations;

import io.github.splotycode.mosaik.annotations.priority.First;
import io.github.splotycode.mosaik.annotations.priority.Last;
import io.github.splotycode.mosaik.annotations.priority.Priority;
import io.github.splotycode.mosaik.annotations.visibility.Invisible;
import io.github.splotycode.mosaik.annotations.visibility.VisibilityLevel;
import io.github.splotycode.mosaik.annotations.visibility.Visible;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;

/**
 * Helper Tools for Mosaik Annotations
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
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
        if (annotation instanceof Last) return Priority.LOWEST;
        if (annotation instanceof First) return Priority.HIGHEST;

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

    /**
     * Checks if a element is visible
     * @param level on with level should we check te visibility
     * @param element the element to check
     * @return true if the element is visible
     */
    public static boolean isVisible(VisibilityLevel level, AnnotatedElement element) {
        switch (level) {
            case FORCE_ALL:
                return true;
            case NONE:
                return false;
            case ONLY_VISIBLE:
                return element.getAnnotation(Visible.class) != null;
            case NOT_INVISIBLE:
                return element.getAnnotation(Invisible.class) == null;
            default:
                throw new IllegalStateException("Unknown VisibilityLevel: " + level.name());
        }
    }

}
