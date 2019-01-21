package io.github.splotycode.mosaik.startup.processbar;

import io.github.splotycode.mosaik.console.ProcessBar;
import io.github.splotycode.mosaik.util.logger.Logger;

public class StartUpProcessHandler {

    private StartUpProcessHandler() {}

    private Logger logger = Logger.getInstance(getClass());

    private static StartUpProcessHandler instance = new StartUpProcessHandler();

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

    public static StartUpProcessHandler getInstance() {
        return instance;
    }
}
