package me.david.davidlib.startup;

import me.david.davidlib.annotation.priority.Priority;
import me.david.davidlib.info.DefaultPathManager;
import me.david.davidlib.link.LinkBase;
import me.david.davidlib.link.Links;
import me.david.davidlib.startup.envirement.StartUpEnvironmentChanger;

@Priority(priority = StartUpPriorities.PRE_LINKBASE)
public class PreLinkBaseStartUp implements StartupTask {

    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        LinkBase.getInstance().registerLink(Links.PATH_MANAGER, new DefaultPathManager());

    }

}
