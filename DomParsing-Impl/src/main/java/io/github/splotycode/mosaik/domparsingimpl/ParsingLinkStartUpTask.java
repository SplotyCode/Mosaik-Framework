package io.github.splotycode.mosaik.domparsingimpl;

import io.github.splotycode.mosaik.annotations.priority.Priority;
import io.github.splotycode.mosaik.domparsingimpl.annotation.FileSystemProviderImpl;
import io.github.splotycode.mosaik.domparsingimpl.annotation.parsing.ReflectiveParsingHelper;
import io.github.splotycode.mosaik.domparsingimpl.formats.json.JsonHandle;
import io.github.splotycode.mosaik.domparsingimpl.formats.keyvalue.KeyValueHandle;
import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Links;
import io.github.splotycode.mosaik.runtime.startup.StartUpPriorities;
import io.github.splotycode.mosaik.runtime.startup.StartupTask;
import io.github.splotycode.mosaik.runtime.startup.environment.StartUpEnvironmentChanger;

@Priority(priority = StartUpPriorities.PRE_LINKBASE)
public class ParsingLinkStartUpTask implements StartupTask {

    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        ParsingManagerImpl manager =  new ParsingManagerImpl();
        manager.register(KeyValueHandle.class);
        manager.register(JsonHandle.class);
        LinkBase.getInstance().registerLink(Links.PARSING_MANAGER, manager);

        LinkBase.getInstance().registerLink(Links.PARSING_FILEPROVIDER, new FileSystemProviderImpl());

        /* EntryParser for Annotation FileSystem */
        ReflectiveParsingHelper.initialize();
    }

}
