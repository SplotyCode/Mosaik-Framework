package io.github.splotycode.mosaik.valuetransformer;

import io.github.splotycode.mosaik.annotations.priority.Priority;
import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Runtime;
import io.github.splotycode.mosaik.runtime.startup.StartUpPriorities;
import io.github.splotycode.mosaik.runtime.startup.StartupTask;
import io.github.splotycode.mosaik.runtime.startup.environment.StartUpEnvironmentChanger;
import io.github.splotycode.mosaik.util.AlmostBoolean;
import io.github.splotycode.mosaik.util.ValueTransformer;
import io.github.splotycode.mosaik.util.reflection.collector.ClassCollector;

@Priority(priority = StartUpPriorities.PRE_LINKBASE)
public class TransformerStartUpTasks implements StartupTask {
    private static ClassCollector collector = ClassCollector.newInstance()
                                            .setAbstracation(AlmostBoolean.NO)
                                            .setInPackage("io.github.splotycode.mosaik.valuetransformer")
                                            .setNoDisable(true)
                                            .setNeedAssignable(ValueTransformer.class);

    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        LinkBase.getInstance().registerLink(TransformerManager.LINK, new TransformerManager());
        TransformerManager.getInstance().registerAll(collector, Runtime.getRuntime().getGlobalClassPath());
    }
}
