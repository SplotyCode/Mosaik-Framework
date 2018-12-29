package me.david.davidlib.startup;

import me.david.davidlib.startup.envirement.StartUpEnvironmentChanger;

public interface StartupTask extends Comparable<StartupTask> {

    void execute(StartUpEnvironmentChanger environmentChanger) throws Exception;

    @Override
    default int compareTo(StartupTask startupTask) {
        return 0;
    }
}
