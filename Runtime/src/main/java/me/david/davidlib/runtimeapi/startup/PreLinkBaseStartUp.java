package me.david.davidlib.runtimeapi.startup;

import me.david.davidlib.runtimeapi.startup.envirement.StartUpEnvironmentChanger;
import me.david.davidlib.util.core.annotation.priority.Priority;
import me.david.davidlib.util.info.DefaultPathManager;
import me.david.davidlib.util.core.link.LinkBase;
import me.david.davidlib.util.core.link.Links;

@Priority(priority = StartUpPriorities.PRE_LINKBASE)
public class PreLinkBaseStartUp implements StartupTask {

    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        LinkBase.getInstance().registerLink(Links.PATH_MANAGER, new DefaultPathManager());

    }

}
