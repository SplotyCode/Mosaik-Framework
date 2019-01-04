package me.david.davidlib.runtime.startup;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import me.david.davidlib.runtime.LinkBase;
import me.david.davidlib.runtime.Links;

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
