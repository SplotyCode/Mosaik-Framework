package de.splotycode.davidlib.argparser;

import me.david.davidlib.annotation.priority.Priority;
import me.david.davidlib.link.LinkBase;
import me.david.davidlib.link.Links;
import me.david.davidlib.startup.StartUpPriorities;
import me.david.davidlib.startup.StartupTask;
import me.david.davidlib.startup.envirement.StartUpEnvironmentChanger;

@Priority(priority = StartUpPriorities.INDEPENDENT_SETUP)
public class ArgParserStartUp implements StartupTask {

    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        LinkBase.getInstance().registerLink(Links.ARG_PARSER, new ArgParser());
    }

}
