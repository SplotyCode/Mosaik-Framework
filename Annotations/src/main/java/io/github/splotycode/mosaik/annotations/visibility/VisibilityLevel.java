package io.github.splotycode.mosaik.annotations.visibility;

/**
 * The {@link VisibilityLevel} specifies witch {@link java.lang.reflect.AnnotatedElement} are visible and with not
 */
public enum VisibilityLevel {

    /**
     * Includes all {@link java.lang.reflect.AnnotatedElement}
     */
    FORCE_ALL,
    /**
     * Includes {@link java.lang.reflect.AnnotatedElement} witch have the {@link Visible} annotation
     */
    ONLY_VISIBLE,
    /**
     * Includes {@link java.lang.reflect.AnnotatedElement} witch have not the {@link Invisible} annotation
     */
    NOT_INVISIBLE,
    /**
     * Includes noting
     */
    NONE

}
