package io.github.splotycode.mosaik.runtime.startup;

import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.Links;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.Map;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class BootContext {

    private String[] args;
    private long startTime;
    private ClassLoaderProvider classLoaderProvider;

    public static ClassLoaderProvider createProvider(String[] args) {
        if (args.length > 1 && args[0].equalsIgnoreCase("-cl")) {
            try {
                return (ClassLoaderProvider) Class.forName(args[1]).newInstance();
            } catch (ReflectiveOperationException e) {
                System.err.println("Failed to load ClassLoaderProvider");
                e.printStackTrace();
            }
        }
        return new ClassLoaderProvider.DefaultClassLoaderProvider();
    }

    public void applyArgs(Object obj) {
        LinkBase.getInstance().getLink(Links.ARG_PARSER).parseArgs(obj, args);
    }

    public void applyArgs(String label, Object obj) {
        LinkBase.getInstance().getLink(Links.ARG_PARSER).parseArgs(obj, label, args);
    }

    public Map<String, String> getArgParameters() {
        return LinkBase.getInstance().getLink(Links.ARG_PARSER).getParameters();
    }

    public Map<String, String> getArgParameters(String label) {
        return LinkBase.getInstance().getLink(Links.ARG_PARSER).getParameters(label);
    }

    public long getUpTime() {
        return System.currentTimeMillis() - startTime;
    }

}
