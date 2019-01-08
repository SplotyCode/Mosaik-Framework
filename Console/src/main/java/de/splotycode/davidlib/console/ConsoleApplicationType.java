package de.splotycode.davidlib.console;

import me.david.davidlib.runtime.application.ApplicationType;
import me.david.davidlib.runtime.startup.BootContext;

public interface ConsoleApplicationType extends ApplicationType {

    default void initType(BootContext context, ConsoleApplicationType dummy) {
    }

    default ProcessBar generateProcessBar(String name, int max, int initial) {
        return new ProcessBar(max, getLogger(), name, initial);
    }

    default ProcessBar generateProcessBar(String name, int max) {
        return generateProcessBar(name, max, 0);
    }

}
