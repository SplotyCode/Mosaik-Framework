package io.github.splotycode.mosaik.runtime.startup.condition;

import io.github.splotycode.mosaik.util.StringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
public abstract class StartupCondition {
    ConditionFailAction failAction;
    @Getter private List<ConditionSignal> signalListener;

    public StartupCondition(ConditionFailAction failAction, ConditionSignal... signalListener) {
        this(failAction, Arrays.asList(signalListener));
    }

    protected abstract TestResult test(ConditionSignal signal);

    void runTest(ConditionSignal signal) {
        if (failAction.isSkipped()) {
            return;
        }

        TestResult result = test(signal);
        if (!result.isSuccessful()) {
            String message = getClass().getSimpleName() + " failed on signal " + signal;
            if (StringUtil.isEmpty(result.getMessage())) {
                message += ": " + result.getMessage();
            }
            failAction.fail(message);
        }
    }
}
