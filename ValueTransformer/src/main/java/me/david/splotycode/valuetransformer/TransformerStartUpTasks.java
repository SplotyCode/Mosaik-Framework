package me.david.splotycode.valuetransformer;

import me.david.davidlib.annotations.priority.Priority;
import me.david.davidlib.runtime.LinkBase;
import me.david.davidlib.runtime.startup.StartUpPriorities;
import me.david.davidlib.runtime.startup.StartupTask;
import me.david.davidlib.runtime.startup.envirement.StartUpEnvironmentChanger;
import me.david.davidlib.util.AlmostBoolean;
import me.david.davidlib.util.reflection.ClassCollector;

@Priority(priority = StartUpPriorities.PRE_LINKBASE)
public class TransformerStartUpTasks implements StartupTask {

    private static ClassCollector collector = ClassCollector.newInstance()
                                            .setAbstracation(AlmostBoolean.NO)
                                            .setInPackage("me.david.splotycode.valuetransformer")
                                            .setNoDisableds(true)
                                            .setNeedAssignable(ValueTransformer.class);

    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        LinkBase.getInstance().registerLink(TransformerManager.LINK, new TransformerManager());
        TransformerManager.getInstance().registerAll(collector);
    }

}
