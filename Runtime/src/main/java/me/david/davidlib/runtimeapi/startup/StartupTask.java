package me.david.davidlib.runtimeapi.startup;

import me.david.davidlib.runtimeapi.startup.envirement.StartUpEnvironmentChanger;

public interface StartupTask extends Comparable<StartupTask> {

    void execute(StartUpEnvironmentChanger environmentChanger) throws Exception;

    @Override
    default int compareTo(StartupTask startupTask) {
        return getClass().getSimpleName().compareTo(startupTask.getClass().getSimpleName());
    }
}
