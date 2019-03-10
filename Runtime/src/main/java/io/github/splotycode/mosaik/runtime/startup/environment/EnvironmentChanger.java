package io.github.splotycode.mosaik.runtime.startup.environment;

import io.github.splotycode.mosaik.runtime.application.Application;

public interface EnvironmentChanger {

    void stopApplicationStart(Class<? extends Application> application);
    void stopApplicationStart(String application);

}
