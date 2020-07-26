package io.github.splotycode.mosaik.runtime.startup.condition.conditions;

import io.github.splotycode.mosaik.runtime.startup.condition.ConditionFailAction;
import io.github.splotycode.mosaik.runtime.startup.condition.ConditionSignal;
import io.github.splotycode.mosaik.runtime.startup.condition.StartupCondition;
import io.github.splotycode.mosaik.runtime.startup.condition.TestResult;
import io.github.splotycode.mosaik.util.reflection.ReflectionUtil;

public class InvokeCondition extends StartupCondition {
    public InvokeCondition() {
        super(ConditionFailAction.LOG_WARN, ConditionSignal.POST_INIT);
    }

    @Override
    protected TestResult test(ConditionSignal signal) {
        if (ReflectionUtil.getCallerClasses().length >= 4 + 1 + 2) {
            return TestResult.failed("Mosaik was not invoked by JVM! It was invoked by: " + ReflectionUtil.getCallerClass(1).getName());
        }
        return TestResult.SUCCESSFUL;
    }
}
