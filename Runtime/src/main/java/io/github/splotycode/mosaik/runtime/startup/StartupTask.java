package io.github.splotycode.mosaik.runtime.startup;

import io.github.splotycode.mosaik.runtime.startup.envirement.StartUpEnvironmentChanger;

public interface StartupTask extends Comparable<StartupTask> {

    void execute(StartUpEnvironmentChanger environmentChanger) throws Exception;

    @Override
    default int compareTo(StartupTask startupTask) {
        return getClass().getSimpleName().compareTo(startupTask.getClass().getSimpleName());
    }
}
