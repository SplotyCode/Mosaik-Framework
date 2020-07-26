package io.github.splotycode.mosaik.runtime.startup.condition.conditions;

import io.github.splotycode.mosaik.runtime.startup.condition.ConditionFailAction;
import io.github.splotycode.mosaik.runtime.startup.condition.ConditionSignal;
import io.github.splotycode.mosaik.runtime.startup.condition.StartupCondition;
import io.github.splotycode.mosaik.runtime.startup.condition.TestResult;
import io.github.splotycode.mosaik.util.StringUtil;

public class ClassLoaderCondition extends StartupCondition {
    public ClassLoaderCondition() {
        super(ConditionFailAction.LOG_WARN, ConditionSignal.POST_INIT);
    }

    @Override
    protected TestResult test(ConditionSignal signal) {
        ClassLoader systemLoader = ClassLoader.getSystemClassLoader();
        ClassLoader threadLoader = Thread.currentThread().getContextClassLoader();
        ClassLoader thisLoader = ClassLoaderCondition.class.getClassLoader();

        if (thisLoader.getClass() != threadLoader.getClass() || thisLoader.getClass() != systemLoader.getClass()) {
            return TestResult.failed(StringUtil.format("Invalid ClassLoader! ThisLoader: '{1}', SystemLoader: '{2}', ThisLoader: '{3}'",
                    className(thisLoader),
                    className(threadLoader),
                    className(systemLoader)));
        }
        return TestResult.SUCCESSFUL;
    }

    private static String className(Object obj) {
        return obj == null ? "Null" : obj.getClass().getName();
    }
}
