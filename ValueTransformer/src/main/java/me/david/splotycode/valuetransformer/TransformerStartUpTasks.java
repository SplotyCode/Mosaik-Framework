package me.david.splotycode.valuetransformer;

import me.david.davidlib.annotations.priority.Priority;
import me.david.davidlib.runtimeapi.LinkBase;
import me.david.davidlib.runtimeapi.Links;
import me.david.davidlib.runtimeapi.startup.StartUpPriorities;
import me.david.davidlib.runtimeapi.startup.StartupTask;
import me.david.davidlib.runtimeapi.startup.envirement.StartUpEnvironmentChanger;

@Priority(priority = StartUpPriorities.PRE_LINKBASE)
public class TransformerStartUpTasks implements StartupTask {

    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        LinkBase.getInstance().registerLink(Links.TRANSFORMER_MANAGER, new TransformerManager());
        LinkBase.getTransformerManager().registerPackage("me.david.splotycode.valuetransformer");
    }

}
