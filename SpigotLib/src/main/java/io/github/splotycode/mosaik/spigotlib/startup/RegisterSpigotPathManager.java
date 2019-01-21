package io.github.splotycode.mosaik.spigotlib.startup;

import io.github.splotycode.mosaik.annotations.priority.Priority;
import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Links;
import io.github.splotycode.mosaik.runtime.startup.StartUpPriorities;
import io.github.splotycode.mosaik.runtime.startup.StartupTask;
import io.github.splotycode.mosaik.runtime.startup.envirement.StartUpEnvironmentChanger;
import io.github.splotycode.mosaik.spigotlib.paths.SpigotPathManager;

@Priority(priority = StartUpPriorities.MANIPULATE_PRE_LINKBASE)
public class RegisterSpigotPathManager implements StartupTask {

    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        LinkBase.getInstance().registerLink(Links.PATH_MANAGER, new SpigotPathManager());
    }

}
