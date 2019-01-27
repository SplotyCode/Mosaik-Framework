package io.github.splotycode.mosaik.domparsingimpl;

import io.github.splotycode.mosaik.annotations.priority.Priority;
import io.github.splotycode.mosaik.domparsing.annotation.parsing.ReflectiveEntry;
import io.github.splotycode.mosaik.domparsing.dom.Node;
import io.github.splotycode.mosaik.domparsingimpl.annotation.FileSystemProviderImpl;
import io.github.splotycode.mosaik.domparsingimpl.readers.keyvalue.KeyValueHandle;
import io.github.splotycode.mosaik.domparsingimpl.readers.keyvalue.dom.KeyValueDocument;
import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Links;
import io.github.splotycode.mosaik.runtime.startup.StartUpPriorities;
import io.github.splotycode.mosaik.runtime.startup.StartupTask;
import io.github.splotycode.mosaik.runtime.startup.envirement.StartUpEnvironmentChanger;
import io.github.splotycode.mosaik.valuetransformer.TransformerManager;

import java.lang.reflect.Field;
import java.util.Map;

@Priority(priority = StartUpPriorities.PRE_LINKBASE)
public class ParsingLinkStartUpTask implements StartupTask {

    @Override
    public void execute(StartUpEnvironmentChanger environmentChanger) throws Exception {
        ParsingManagerImpl manager =  new ParsingManagerImpl();
        manager.register(KeyValueHandle.class);
        LinkBase.getInstance().registerLink(Links.PARSING_MANAGER, manager);

        LinkBase.getInstance().registerLink(Links.PARSING_FILEPROVIDER, new FileSystemProviderImpl());

        /* ReflectiveEntry for Annotation FileSystem */
        ReflectiveEntry.setReadFun(data -> {
            try {
                KeyValueDocument document = new KeyValueDocument();
                for (Map.Entry<String, String> node : data.getOne().entrySet()) {
                    Field field = data.getTwo().getClass().getDeclaredField(node.getValue());
                    field.setAccessible(true);
                    document.addNodeWithInnerText(node.getKey(), LinkBase.getInstance().getLink(TransformerManager.LINK).transform(field.get(data.getTwo()), String.class));
                }
                return document;
            } catch (ReflectiveOperationException ex) {
                ex.printStackTrace();
                return null;
            }
        });
        ReflectiveEntry.setWriteFun(data -> {
            try {
                for (Node node : data.getTwo().getNodes()) {
                    String key = node.name();
                    String value = data.getTwo().getFirstTextFromNode(key);

                    Field field = getClass().getDeclaredField(data.getOne().getFieldName(key));
                    field.setAccessible(true);
                    field.set(this, LinkBase.getInstance().getLink(TransformerManager.LINK).transform(value, field.getType()));
                }
            } catch (ReflectiveOperationException ex) {
                ex.printStackTrace();
            }
        });
    }

}
