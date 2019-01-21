package io.github.splotycode.mosaik.domparsing;

import io.github.splotycode.mosaik.domparsing.keyvalue.KeyValueHandle;
import io.github.splotycode.mosaik.annotations.priority.Priority;
import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Links;
import io.github.splotycode.mosaik.runtime.startup.StartUpPriorities;
import io.github.splotycode.mosaik.runtime.startup.StartupTask;
import io.github.splotycode.mosaik.runtime.startup.envirement.StartUpEnvironmentChanger;

@Priority(priority = StartUpPriorities.PRE_LINKBASE)
public class ParsingLinkStartUpTask implements StartupTask {

    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        ParsingManagerImpl manager =  new ParsingManagerImpl();
        manager.register(KeyValueHandle.class);
        LinkBase.getInstance().registerLink(Links.PARSING_MANAGER, manager);
    }
}
