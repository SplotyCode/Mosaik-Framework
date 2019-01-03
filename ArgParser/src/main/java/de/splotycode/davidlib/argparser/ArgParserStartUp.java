package de.splotycode.davidlib.argparser;

import me.david.davidlib.annotations.priority.Priority;
import me.david.davidlib.runtimeapi.LinkBase;
import me.david.davidlib.runtimeapi.Links;
import me.david.davidlib.runtimeapi.startup.StartUpPriorities;
import me.david.davidlib.runtimeapi.startup.StartupTask;
import me.david.davidlib.runtimeapi.startup.envirement.StartUpEnvironmentChanger;

@Priority(priority = StartUpPriorities.INDEPENDENT_SETUP)
public class ArgParserStartUp implements StartupTask {

    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        LinkBase.getInstance().registerLink(Links.ARG_PARSER, new ArgParser());
    }

}
