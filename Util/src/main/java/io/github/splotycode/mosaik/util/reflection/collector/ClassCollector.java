package io.github.splotycode.mosaik.util.reflection.collector;

import io.github.splotycode.mosaik.annotations.visibility.VisibilityLevel;
import io.github.splotycode.mosaik.util.AlmostBoolean;
import io.github.splotycode.mosaik.util.condition.ClassConditions;
import io.github.splotycode.mosaik.util.condition.Conditions;
import io.github.splotycode.mosaik.util.io.resource.ClassPathResource;
import io.github.splotycode.mosaik.util.reflection.classpath.ClassPath;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClassCollector implements Predicate<Class> {
    public static ClassCollector newInstance() {
        return new ClassCollector();
    }

    private Map<Integer, Predicate<? super Class>> conditions = new HashMap<>();
    private Predicate<Class> lastCondition = null;
    private int costomCounter = 0;

    public Class collectFirst(ClassPath classPath) {
        return streamClasses(classPath).findFirst().orElse(null);
    }

    protected Stream<Class> streamClasses(ClassPath classPath) {
        return classPath.classes().map(ClassPathResource::loadClass).filter(this);
    }

    public Collection<Class> collectAll(ClassPath classPath) {
        return streamClasses(classPath).collect(Collectors.toList());
    }

    public Collection collectAllInstances(ClassPath classPath) {
        return streamClasses(classPath).map(this::createInstance).collect(Collectors.toList());
    }

    Object createInstance(Class clazz) {
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new IllegalStateException("Can not create instance", e);
        }
    }

    public Predicate<Class> buildCondition() {
        if (lastCondition == null) {
            lastCondition = buildCondition0();
        }
        return lastCondition;
    }

    private Predicate<Class> buildCondition0() {
        return Conditions.and(conditions.values());
    }

    protected void reset() {
        lastCondition = null;
    }

    public ClassCollector setAbstracation(AlmostBoolean allow) {
        reset();
        switch (allow) {
            case NO:
                conditions.put(0, ClassConditions.NOT_ABSTRACT);
                break;
            case YES:
                conditions.put(0, ClassConditions.NEED_ABSTRACT);
                break;
            case MAYBE:
                conditions.remove(0);
                break;
        }
        return this;
    }

    public ClassCollector setOnlyClasses(boolean onlyClasses) {
        reset();
        if (onlyClasses) {
            conditions.put(1, ClassConditions.MUST_BE_CLASS);
        } else {
            conditions.remove(1);
        }
        return this;
    }

    public ClassCollector setNeedAssignable(Class clazz) {
        reset();
        if (clazz == null) {
            conditions.remove(2);
        } else {
            conditions.put(2, ClassConditions.assignable(clazz));
        }
        return this;
    }

    public ClassCollector setNoDisable(boolean noDisable) {
        reset();
        if (noDisable) {
            conditions.put(3, ClassConditions.NO_DISABLE_ANNOTATION);
        } else {
            conditions.remove(3);
        }
        return this;
    }

    public ClassCollector setInPackage(String packageName) {
        reset();
        if (packageName == null) {
            conditions.remove(4);
        } else {
            conditions.put(4, ClassConditions.isInPackage(packageName));
        }
        return this;
    }

    public ClassCollector setVisibility(VisibilityLevel level) {
        reset();
        if (level == null || level == VisibilityLevel.FORCE_ALL) {
            conditions.remove(5);
        } else {
            conditions.put(5, ClassConditions.visisble(level));
        }
        return this;
    }

    public ClassCollector addCostom(Predicate<Class> condition) {
        reset();
        conditions.put(100 + costomCounter++, condition);
        return this;
    }

    public long totalResults(ClassPath classPath) {
        return streamClasses(classPath).count();
    }

    @Override
    public boolean test(Class item) {
        return buildCondition().test(item);
    }
}
