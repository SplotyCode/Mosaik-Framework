package io.github.splotycode.mosaik.valuetransformer;

import io.github.splotycode.mosaik.annotations.priority.Priority;
import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Runtime;
import io.github.splotycode.mosaik.runtime.startup.StartUpPriorities;
import io.github.splotycode.mosaik.runtime.startup.StartupTask;
import io.github.splotycode.mosaik.runtime.startup.environment.StartUpEnvironmentChanger;
import io.github.splotycode.mosaik.valuetransformer.impl.ValueConverterImpl;

@Priority(priority = StartUpPriorities.PRE_LINKBASE)
public class TransformerStartUpTasks implements StartupTask {
    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        LinkBase.getInstance().registerLink(TransformerManager.LINK, new TransformerManager());
        TransformerManager.getInstance().registerAll(ValueConverterImpl.COMMON_COLLECTOR,
                Runtime.getRuntime().getGlobalClassPath());
    }
}
