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
