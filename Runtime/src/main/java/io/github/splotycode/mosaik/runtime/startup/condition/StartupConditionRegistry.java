package io.github.splotycode.mosaik.runtime.startup.condition;

import io.github.splotycode.mosaik.runtime.startup.StartUpConfiguration;
import io.github.splotycode.mosaik.runtime.startup.condition.conditions.ModuleLoadedCondition;
import io.github.splotycode.mosaik.util.collection.MultiHashMap;

import java.util.HashMap;
import java.util.Map;

public class StartupConditionRegistry {
    public static StartupConditionRegistry fromConfiguration(StartUpConfiguration config) {
        StartupConditionRegistry registry = new StartupConditionRegistry();
        registry.applyCondiguration(config.getConditionActions());
        return registry;
    }

    private Map<Class<? extends StartupCondition>, StartupCondition> conditionMap = new HashMap<>();
    private MultiHashMap<ConditionSignal, StartupCondition> startupConditions = new MultiHashMap<>();

    {
        register(new ModuleLoadedCondition());
    }

    public void applyCondiguration(Map<Class<? extends StartupCondition>, ConditionFailAction> config) {
        for (Map.Entry<Class<? extends StartupCondition>, ConditionFailAction> condition : config.entrySet()) {
            conditionMap.get(condition.getKey()).failAction = condition.getValue();
        }
    }

    private void register(StartupCondition condition) {
        conditionMap.put(condition.getClass(), condition);
        for (ConditionSignal signal : condition.getSignalListener()) {
            startupConditions.addToList(signal, condition);
        }
    }

    public void testSignal(ConditionSignal signal) {
        startupConditions.get(signal).forEach(condition -> condition.runTest(signal));
    }
}
