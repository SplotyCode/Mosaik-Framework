package me.david.splotycode.valuetransformer;

import me.david.davidlib.annotations.priority.Priority;
import me.david.davidlib.runtime.LinkBase;
import me.david.davidlib.runtime.startup.StartUpPriorities;
import me.david.davidlib.runtime.startup.StartupTask;
import me.david.davidlib.runtime.startup.envirement.StartUpEnvironmentChanger;

@Priority(priority = StartUpPriorities.PRE_LINKBASE)
public class TransformerStartUpTasks implements StartupTask {

    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        LinkBase.getInstance().registerLink(TransformerManager.LINK, new TransformerManager());
        TransformerManager.getInstance().registerPackage("me.david.splotycode.valuetransformer");
    }

}
