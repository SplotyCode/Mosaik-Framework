package de.splotycode.davidlib.domparsing;

import de.splotycode.davidlib.domparsing.keyvalue.KeyValueHandle;
import me.david.davidlib.link.LinkBase;
import me.david.davidlib.link.Links;
import me.david.davidlib.startup.StartupTask;
import me.david.davidlib.startup.envirement.StartUpEnvironmentChanger;

public class ParsingLinkStartUpTask implements StartupTask {

    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        ParsingManagerImpl manager =  new ParsingManagerImpl();
        manager.register(KeyValueHandle.class);
        LinkBase.getInstance().registerLink(Links.PARSING_MANAGER, manager);
    }
}
