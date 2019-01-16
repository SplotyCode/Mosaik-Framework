package me.david.davidlib.util.reflection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.david.davidlib.util.AlmostBoolean;
import me.david.davidlib.util.condition.ClassConditions;
import me.david.davidlib.util.condition.Condition;
import me.david.davidlib.util.condition.Conditions;

import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClassCollector implements Condition<Class> {

    private Map<Integer, Condition<Class>> conditions = new HashMap<>();
    private boolean change = true;
    private Condition<Class> lastCondition = null;

    public static ClassCollector newInstance() {
        return new ClassCollector();
    }

    public Condition<Class> buildCondition() {
        if (change || lastCondition == null) {
            lastCondition = buildCondition0();
            change = false;
        }
        return lastCondition;
    }

    private Condition<Class> buildCondition0() {
        return Conditions.and(conditions.values());
    }

    public ClassCollector setAbstracation(AlmostBoolean allow) {
        change = true;
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
        if (onlyClasses) {
            conditions.put(1, ClassConditions.MUST_BE_CLASS);
        } else {
            conditions.remove(1);
        }
        return this;
    }

    public ClassCollector setNeedAssignable(Class clazz) {
        if (clazz == null) {
            conditions.remove(2);
        } else {
            conditions.put(2, ClassConditions.assignable(clazz));
        }
        return this;
    }

    public ClassCollector setNoDisableds(boolean noDisableds) {
        if (noDisableds) {
            conditions.put(3, ClassConditions.NO_DISABLE_ANNOTATION);
        } else {
            conditions.remove(3);
        }
        return this;
    }

    @Override
    public boolean check(Class item) {
        return buildCondition().check(item);
    }
}
