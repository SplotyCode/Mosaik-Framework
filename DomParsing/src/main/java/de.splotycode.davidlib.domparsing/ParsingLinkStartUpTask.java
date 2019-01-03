package de.splotycode.davidlib.domparsing;

import de.splotycode.davidlib.domparsing.keyvalue.KeyValueHandle;
import me.david.davidlib.util.core.annotation.priority.Priority;
import me.david.davidlib.util.core.link.LinkBase;
import me.david.davidlib.util.core.link.Links;
import me.david.davidlib.util.core.startup.StartUpPriorities;
import me.david.davidlib.util.core.startup.StartupTask;
import me.david.davidlib.util.core.startup.envirement.StartUpEnvironmentChanger;

@Priority(priority = StartUpPriorities.PRE_LINKBASE)
public class ParsingLinkStartUpTask implements StartupTask {

    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        ParsingManagerImpl manager =  new ParsingManagerImpl();
        manager.register(KeyValueHandle.class);
        LinkBase.getInstance().registerLink(Links.PARSING_MANAGER, manager);
    }
}
