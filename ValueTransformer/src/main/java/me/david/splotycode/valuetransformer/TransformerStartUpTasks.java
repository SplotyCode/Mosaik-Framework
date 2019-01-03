package me.david.splotycode.valuetransformer;

import me.david.davidlib.util.core.annotation.priority.Priority;
import me.david.davidlib.util.core.link.LinkBase;
import me.david.davidlib.util.core.link.Links;
import me.david.davidlib.util.core.startup.StartUpPriorities;
import me.david.davidlib.util.core.startup.StartupTask;
import me.david.davidlib.util.core.startup.envirement.StartUpEnvironmentChanger;

@Priority(priority = StartUpPriorities.PRE_LINKBASE)
public class TransformerStartUpTasks implements StartupTask {

    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        LinkBase.getInstance().registerLink(Links.TRANSFORMER_MANAGER, new TransformerManager());
        LinkBase.getTransformerManager().registerPackage("me.david.splotycode.valuetransformer");
    }

}
