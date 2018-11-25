package de.splotycode.davidlib.startup.processbar;

import de.splotycode.davidlib.console.ProcessBar;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StartUpProcessHandler {

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
        processBar = new ProcessBar(subSize, System.out, name, 1);
    }

    public void end() {
        processBar.stop();
    }

}
