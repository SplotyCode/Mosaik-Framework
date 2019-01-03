package de.splotycode.davidlib.startup.processbar;

import de.splotycode.davidlib.console.ProcessBar;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.david.davidlib.util.logger.Logger;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StartUpProcessHandler {

    private Logger logger = Logger.getInstance(getClass());

    @Getter private static StartUpProcessHandler instance = new StartUpProcessHandler();

    private ProcessBar processBar;

    public ProcessBar current() {
        return processBar;
    }

    public void next() {
        processBar.step();
    }

    public void newProcess(String name, int subSize) {
        if (processBar != null) processBar.stop();
        processBar = new ProcessBar(subSize, logger, name, 1);
    }

    public void end() {
        processBar.stop();
    }

}
