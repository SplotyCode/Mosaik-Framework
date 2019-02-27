package io.github.splotycode.mosaik.startup.envirementchanger;

import io.github.splotycode.mosaik.runtime.application.Application;
import io.github.splotycode.mosaik.runtime.startup.environment.StartUpEnvironmentChanger;
import io.github.splotycode.mosaik.startup.application.ApplicationHandleImpl;

public class StartUpInvirementChangerImpl extends AbstractEnvironmentChanger implements StartUpEnvironmentChanger {

    @Override
    public void stopApplicationStart(Class<? extends Application> application) {
        ApplicationHandleImpl.getSkippedClasses().add(application);
    }

    @Override
    public void stopApplicationStart(String application) {
        ApplicationHandleImpl.getSkippedNames().add(application);
    }
}
