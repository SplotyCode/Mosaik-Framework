package me.david.davidlib.runtime.startup;

import me.david.davidlib.annotations.priority.Priority;
import me.david.davidlib.runtime.LinkBase;
import me.david.davidlib.runtime.pathmanager.DefaultPathManager;
import me.david.davidlib.runtime.startup.envirement.StartUpEnvironmentChanger;
import me.david.davidlib.runtime.Links;

@Priority(priority = StartUpPriorities.PRE_LINKBASE)
public class PreLinkBaseStartUp implements StartupTask {

    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        LinkBase.getInstance().registerLink(Links.PATH_MANAGER, new DefaultPathManager());
    }

}
