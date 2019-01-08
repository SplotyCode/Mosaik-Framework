package de.splotycode.davidlib.domparsing;

import de.splotycode.davidlib.domparsing.keyvalue.KeyValueHandle;
import me.david.davidlib.annotations.priority.Priority;
import me.david.davidlib.runtime.LinkBase;
import me.david.davidlib.runtime.Links;
import me.david.davidlib.runtime.startup.StartUpPriorities;
import me.david.davidlib.runtime.startup.StartupTask;
import me.david.davidlib.runtime.startup.envirement.StartUpEnvironmentChanger;

@Priority(priority = StartUpPriorities.PRE_LINKBASE)
public class ParsingLinkStartUpTask implements StartupTask {

    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        ParsingManagerImpl manager =  new ParsingManagerImpl();
        manager.register(KeyValueHandle.class);
        LinkBase.getInstance().registerLink(Links.PARSING_MANAGER, manager);
    }
}
