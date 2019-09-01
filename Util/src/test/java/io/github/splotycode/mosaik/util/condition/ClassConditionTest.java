package io.github.splotycode.mosaik.util.condition;

import io.github.splotycode.mosaik.annotations.Disabled;
import io.github.splotycode.mosaik.annotations.SkipPath;
import io.github.splotycode.mosaik.annotations.visibility.Invisible;
import io.github.splotycode.mosaik.annotations.visibility.VisibilityLevel;
import io.github.splotycode.mosaik.annotations.visibility.Visible;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ClassConditionTest {

    public class NormalClass {}
    public class ExtentsNormal extends NormalClass {}

    public abstract class AbstractClass {}

    @Disabled
    public class DisabledClass {}

    @SkipPath("")
    public class WithSkippedPath {}

    @Visible @Invisible
    public class MultipleAnnotations {}

    public class MethodClass {
        void one() {}
        void two() {}
    }

    @Visible
    public class VisibleClass {}

    @Invisible
    public class InvisibleClass {}

    public @interface AnAnnotation {}
    public enum AnEnum {}
    public interface AnInterface {}

    @Test
    void testAbstraction() {
        assertTrue(ClassConditions.NEED_ABSTRACT.test(AbstractClass.class));
        assertFalse(ClassConditions.NEED_ABSTRACT.test(NormalClass.class));

        assertFalse(ClassConditions.NOT_ABSTRACT.test(AbstractClass.class));
        assertTrue(ClassConditions.NOT_ABSTRACT.test(NormalClass.class));
    }

    @Test
    void testMustBeClass() {
        assertTrue(ClassConditions.MUST_BE_CLASS.test(NormalClass.class));
        assertTrue(ClassConditions.MUST_BE_CLASS.test(AbstractClass.class));

        assertFalse(ClassConditions.MUST_BE_CLASS.test(AnAnnotation.class));
        assertFalse(ClassConditions.MUST_BE_CLASS.test(AnEnum.class));
        assertFalse(ClassConditions.MUST_BE_CLASS.test(AnInterface.class));


        assertTrue(ClassConditions.mustBeClass().test(NormalClass.class));
        assertTrue(ClassConditions.mustBeClass().test(AbstractClass.class));

        assertFalse(ClassConditions.mustBeClass().test(AnAnnotation.class));
        assertFalse(ClassConditions.mustBeClass().test(AnEnum.class));
        assertFalse(ClassConditions.mustBeClass().test(AnInterface.class));
    }

    @Test
    void testInPackage() {
        assertFalse(ClassConditions.isInPackage("abc").test(ClassConditionTest.class));
        assertFalse(ClassConditions.isInPackage("abc.abc").test(ClassConditionTest.class));

        assertTrue(ClassConditions.isInPackage("io.github").test(ClassConditionTest.class));
        assertTrue(ClassConditions.isInPackage("io.github.splotycode.mosaik.util.condition").test(ClassConditionTest.class));
    }

    @Test
    void testAssignable() {
        assertTrue(ClassConditions.assignable(NormalClass.class).test(ExtentsNormal.class));
        assertFalse(ClassConditions.assignable(NormalClass.class).test(ClassConditionTest.class));

        assertTrue(ClassConditions.assignableClass(NormalClass.class).test(new ExtentsNormal()));
    }

    @Test
    void testInstanceOf() {
        assertTrue(ClassConditions.instanceOf(NormalClass.class).test(new NormalClass()));
        assertFalse(ClassConditions.instanceOf(NormalClass.class).test(new AbstractClass() {}));

        assertTrue(ClassConditions.instanceOf(new NormalClass()).test(NormalClass.class));
        assertFalse(ClassConditions.instanceOf(new AbstractClass() {}).test(NormalClass.class));
    }


    @Test
    void testAnnotation() {
        assertTrue(ClassConditions.NO_DISABLE_ANNOTATION.test(NormalClass.class));
        assertFalse(ClassConditions.NO_DISABLE_ANNOTATION.test(DisabledClass.class));

        assertTrue(ClassConditions.needAnnotation(SkipPath.class).test(WithSkippedPath.class));
        assertFalse(ClassConditions.needAnnotation(SkipPath.class).test(NormalClass.class));

        assertTrue(ClassConditions.needAnnotations(Invisible.class, Visible.class).test(MultipleAnnotations.class));
        assertFalse(ClassConditions.needAnnotations(Invisible.class, Visible.class).test(VisibleClass.class));
        assertFalse(ClassConditions.needAnnotations(Invisible.class, Visible.class).test(NormalClass.class));

        assertTrue(ClassConditions.needOneAnnotation(Invisible.class, Visible.class).test(MultipleAnnotations.class));
        assertTrue(ClassConditions.needOneAnnotation(Invisible.class, Visible.class).test(VisibleClass.class));
        assertFalse(ClassConditions.needOneAnnotation(Invisible.class, Visible.class).test(NormalClass.class));

        assertTrue(ClassConditions.unallowedAnnotation(Invisible.class).test(VisibleClass.class));
        assertTrue(ClassConditions.unallowedAnnotation(Invisible.class).test(NormalClass.class));
        assertFalse(ClassConditions.unallowedAnnotation(Visible.class).test(VisibleClass.class));
        assertFalse(ClassConditions.unallowedAnnotation(Visible.class).test(MultipleAnnotations.class));

        assertTrue(ClassConditions.needAnnotations(Arrays.asList(Invisible.class, Visible.class)).test(MultipleAnnotations.class));
        assertFalse(ClassConditions.needAnnotations(Arrays.asList(Invisible.class, Visible.class)).test(VisibleClass.class));
        assertFalse(ClassConditions.needAnnotations(Arrays.asList(Invisible.class, Visible.class)).test(NormalClass.class));

        assertTrue(ClassConditions.needOneAnnotation(Arrays.asList(Invisible.class, Visible.class)).test(MultipleAnnotations.class));
        assertTrue(ClassConditions.needOneAnnotation(Arrays.asList(Invisible.class, Visible.class)).test(VisibleClass.class));
        assertFalse(ClassConditions.needOneAnnotation(Arrays.asList(Invisible.class, Visible.class)).test(NormalClass.class));
    }

    @Test
    void testMethods() {
        assertFalse(ClassConditions.anyMethod(method -> true).test(NormalClass.class));
        assertTrue(ClassConditions.allMethods(method -> true).test(NormalClass.class));

        assertTrue(ClassConditions.anyMethod(method -> true).test(MethodClass.class));
        assertTrue(ClassConditions.allMethods(method -> true).test(MethodClass.class));

        assertTrue(ClassConditions.anyMethod(method -> method.getName().equals("one")).test(MethodClass.class));
        assertFalse(ClassConditions.allMethods(method -> method.getName().equals("one")).test(MethodClass.class));
    }

    @Test
    void testVisibility() {
        assertFalse(ClassConditions.visisble(VisibilityLevel.NONE).test(NormalClass.class));
        assertFalse(ClassConditions.visisble(VisibilityLevel.NONE).test(VisibleClass.class));
        assertFalse(ClassConditions.visisble(VisibilityLevel.NONE).test(InvisibleClass.class));

        assertTrue(ClassConditions.visisble(VisibilityLevel.FORCE_ALL).test(NormalClass.class));
        assertTrue(ClassConditions.visisble(VisibilityLevel.FORCE_ALL).test(VisibleClass.class));
        assertTrue(ClassConditions.visisble(VisibilityLevel.FORCE_ALL).test(InvisibleClass.class));

        assertTrue(ClassConditions.visisble(VisibilityLevel.NOT_INVISIBLE).test(NormalClass.class));
        assertTrue(ClassConditions.visisble(VisibilityLevel.NOT_INVISIBLE).test(VisibleClass.class));
        assertFalse(ClassConditions.visisble(VisibilityLevel.NOT_INVISIBLE).test(InvisibleClass.class));

        assertFalse(ClassConditions.visisble(VisibilityLevel.ONLY_VISIBLE).test(NormalClass.class));
        assertTrue(ClassConditions.visisble(VisibilityLevel.ONLY_VISIBLE).test(VisibleClass.class));
        assertFalse(ClassConditions.visisble(VisibilityLevel.ONLY_VISIBLE).test(InvisibleClass.class));
    }

}
