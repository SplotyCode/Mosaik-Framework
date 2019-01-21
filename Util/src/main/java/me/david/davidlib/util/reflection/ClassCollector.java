package me.david.davidlib.util.reflection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.david.davidlib.util.AlmostBoolean;
import me.david.davidlib.util.condition.ClassConditions;
import me.david.davidlib.util.condition.Conditions;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClassCollector implements Predicate<Class> {

    private Map<Integer, Predicate<Class>> conditions = new HashMap<>();
    private Predicate<Class> lastCondition = null;
    private Collection<Class> lastFetchedClasses = null;
    private Collection<Object> lastFetchedInstances = null;

    private int costomCounter = 0;

    public static ClassCollector newInstance() {
        return new ClassCollector();
    }

    public Class collectFirst() {
        return ClassFinderHelper.getUserClasses().stream().filter(this::test).findFirst().orElse(null);
    }

    public Collection<Class> collectAll() {
        if (lastFetchedClasses == null) {
            lastFetchedClasses = collectAll0();
        }
        return lastFetchedClasses;
    }

    public Collection collectAllInstances() {
        if (lastFetchedInstances == null) {
            lastFetchedInstances = collectAllInstances0();
        }
        return lastFetchedInstances;
    }

    private Collection<Object> collectAllInstances0() {
        return collectAll().stream().map(clazz -> {
            try {
                return clazz.newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException("Can not create instance", e);
            }
        }).collect(Collectors.toList());
    }

    private Collection<Class> collectAll0() {
        return ClassFinderHelper.getUserClasses().stream().filter(this::test).collect(Collectors.toList());
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

    private void reset() {
        lastCondition = null;
        lastFetchedClasses = null;
        lastFetchedInstances = null;
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

    public ClassCollector setNoDisableds(boolean noDisableds) {
        reset();
        if (noDisableds) {
            conditions.put(3, ClassConditions.NO_DISABLE_ANNOTATION);
        } else {
            conditions.remove(3);
        }
        return this;
    }

    public ClassCollector setInPackage(String packageName) {
        if (packageName == null) {
            conditions.remove(4);
        } else {
            conditions.put(4, ClassConditions.isInPackage(packageName));
        }
        return this;
    }

    public ClassCollector addCostom(Predicate<Class> condition) {
        conditions.put(100 + costomCounter, condition);
        costomCounter++;
        return this;
    }

    public int totalResults() {
        return collectAll().size();
    }

    @Override
    public boolean test(Class item) {
        return buildCondition().test(item);
    }
}
