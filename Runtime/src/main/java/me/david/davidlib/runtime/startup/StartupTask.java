package me.david.davidlib.runtime.startup;

import me.david.davidlib.runtime.startup.envirement.StartUpEnvironmentChanger;

public interface StartupTask extends Comparable<StartupTask> {

    void execute(StartUpEnvironmentChanger environmentChanger) throws Exception;

    @Override
    default int compareTo(StartupTask startupTask) {
        return getClass().getSimpleName().compareTo(startupTask.getClass().getSimpleName());
    }
}
