package de.splotycode.davidlib.argparserimpl;

import me.david.davidlib.annotations.priority.Priority;
import me.david.davidlib.runtime.LinkBase;
import me.david.davidlib.runtime.Links;
import me.david.davidlib.runtime.startup.StartUpPriorities;
import me.david.davidlib.runtime.startup.StartupTask;
import me.david.davidlib.runtime.startup.envirement.StartUpEnvironmentChanger;

/**
 * Register's the ArgParser to the LinkBase
 */
@Priority(priority = StartUpPriorities.INDEPENDENT_SETUP)
public class ArgParserStartUp implements StartupTask {

    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        LinkBase.getInstance().registerLink(Links.ARG_PARSER, new ArgParser());
    }

}
