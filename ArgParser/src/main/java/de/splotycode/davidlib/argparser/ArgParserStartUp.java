package de.splotycode.davidlib.argparser;

import me.david.davidlib.util.core.annotation.priority.Priority;
import me.david.davidlib.util.core.link.LinkBase;
import me.david.davidlib.util.core.link.Links;
import me.david.davidlib.util.core.startup.StartUpPriorities;
import me.david.davidlib.util.core.startup.StartupTask;
import me.david.davidlib.util.core.startup.envirement.StartUpEnvironmentChanger;

@Priority(priority = StartUpPriorities.INDEPENDENT_SETUP)
public class ArgParserStartUp implements StartupTask {

    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        LinkBase.getInstance().registerLink(Links.ARG_PARSER, new ArgParser());
    }

}
