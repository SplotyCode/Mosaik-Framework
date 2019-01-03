package me.david.davidlib.runtimeapi.startup;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.david.davidlib.util.core.link.LinkBase;
import me.david.davidlib.util.core.link.Links;

@EqualsAndHashCode
@AllArgsConstructor
@Getter
public class BootContext {

    private String[] args;
    private long startTime;

    public void applyArgs(Object obj) {
        LinkBase.getInstance().getLink(Links.ARG_PARSER).parseArgs(obj, args);
    }

    public void applyArgs(String label, Object obj) {
        LinkBase.getInstance().getLink(Links.ARG_PARSER).parseArgs(obj, label, args);
    }

    public long getUpTime() {
        return System.currentTimeMillis() - startTime;
    }

}
