package me.david.davidlib.spigotlib.startup;

import me.david.davidlib.annotation.priority.Priority;
import me.david.davidlib.link.LinkBase;
import me.david.davidlib.link.Links;
import me.david.davidlib.spigotlib.paths.SpigotPathManager;
import me.david.davidlib.startup.StartUpPriorities;
import me.david.davidlib.startup.StartupTask;
import me.david.davidlib.startup.envirement.StartUpEnvironmentChanger;

@Priority(priority = StartUpPriorities.MANIPULATE_PRE_LINKBASE)
public class RegisterSpigotPathManager implements StartupTask {

    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        LinkBase.getInstance().registerLink(Links.PATH_MANAGER, new SpigotPathManager());
    }

}
