package me.david.davidlib.spigotlib.startup;

import me.david.davidlib.annotations.priority.Priority;
import me.david.davidlib.runtimeapi.LinkBase;
import me.david.davidlib.runtimeapi.Links;
import me.david.davidlib.runtimeapi.startup.StartUpPriorities;
import me.david.davidlib.runtimeapi.startup.StartupTask;
import me.david.davidlib.runtimeapi.startup.envirement.StartUpEnvironmentChanger;
import me.david.davidlib.spigotlib.paths.SpigotPathManager;

@Priority(priority = StartUpPriorities.MANIPULATE_PRE_LINKBASE)
public class RegisterSpigotPathManager implements StartupTask {

    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        LinkBase.getInstance().registerLink(Links.PATH_MANAGER, new SpigotPathManager());
    }

}
