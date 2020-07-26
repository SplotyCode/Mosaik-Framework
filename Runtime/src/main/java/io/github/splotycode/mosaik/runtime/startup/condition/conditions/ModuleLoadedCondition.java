package io.github.splotycode.mosaik.runtime.startup.condition.conditions;

import io.github.splotycode.mosaik.runtime.startup.condition.ConditionFailAction;
import io.github.splotycode.mosaik.runtime.startup.condition.ConditionSignal;
import io.github.splotycode.mosaik.runtime.startup.condition.StartupCondition;
import io.github.splotycode.mosaik.runtime.startup.condition.TestResult;
import io.github.splotycode.mosaik.runtime.module.MosaikModule;

public class ModuleLoadedCondition extends StartupCondition {
    private static final MosaikModule[] REQUIRED_MODULES = new MosaikModule[] {
            MosaikModule.STARTUP, MosaikModule.DOM_PARSING_IMPL,
            MosaikModule.ARG_PARSER_IMPL};

    public ModuleLoadedCondition() {
        super(ConditionFailAction.RAISE_ERROR, ConditionSignal.PRE_INIT);
    }

    @Override
    protected TestResult test(ConditionSignal signal) {
        for (MosaikModule module : REQUIRED_MODULES) {
            String loadError = module.getLoadError();
            if (loadError != null) {
                return TestResult.failed(loadError);
            }
        }
        return TestResult.SUCCESSFUL;
    }
}
