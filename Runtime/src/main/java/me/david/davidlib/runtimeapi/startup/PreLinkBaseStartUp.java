package me.david.davidlib.runtimeapi.startup;

import me.david.davidlib.annotations.priority.Priority;
import me.david.davidlib.runtimeapi.LinkBase;
import me.david.davidlib.runtimeapi.Links;
import me.david.davidlib.runtimeapi.pathmanager.DefaultPathManager;
import me.david.davidlib.runtimeapi.startup.envirement.StartUpEnvironmentChanger;

@Priority(priority = StartUpPriorities.PRE_LINKBASE)
public class PreLinkBaseStartUp implements StartupTask {

    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        LinkBase.getInstance().registerLink(Links.PATH_MANAGER, new DefaultPathManager());
    }

}
