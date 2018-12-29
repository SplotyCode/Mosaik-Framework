package me.david.splotycode.valuetransformer;

import me.david.davidlib.annotation.priority.Priority;
import me.david.davidlib.link.LinkBase;
import me.david.davidlib.link.Links;
import me.david.davidlib.startup.StartUpPriorities;
import me.david.davidlib.startup.StartupTask;
import me.david.davidlib.startup.envirement.StartUpEnvironmentChanger;

@Priority(priority = StartUpPriorities.PRE_LINKBASE)
public class TransformerStartUpTasks implements StartupTask {

    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        LinkBase.getInstance().registerLink(Links.TRANSFORMER_MANAGER, new TransformerManager());
        LinkBase.getTransformerManager().registerPackage("me.david.splotycode.valuetransformer");
    }

}
