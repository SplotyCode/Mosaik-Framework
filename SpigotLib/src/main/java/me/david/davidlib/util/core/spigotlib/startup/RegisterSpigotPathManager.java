package me.david.davidlib.util.core.spigotlib.startup;

import me.david.davidlib.util.core.annotation.priority.Priority;
import me.david.davidlib.util.core.link.LinkBase;
import me.david.davidlib.util.core.link.Links;
import me.david.davidlib.util.core.spigotlib.paths.SpigotPathManager;
import me.david.davidlib.util.core.startup.StartUpPriorities;
import me.david.davidlib.util.core.startup.StartupTask;
import me.david.davidlib.util.core.startup.envirement.StartUpEnvironmentChanger;

@Priority(priority = StartUpPriorities.MANIPULATE_PRE_LINKBASE)
public class RegisterSpigotPathManager implements StartupTask {

    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        LinkBase.getInstance().registerLink(Links.PATH_MANAGER, new SpigotPathManager());
    }

}
