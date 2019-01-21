package io.github.splotycode.mosaik.runtime.startup.envirement;

import io.github.splotycode.mosaik.runtime.application.Application;

public interface ConfiguriseEnvironmentChanger {

    void stopApplicationStart(Class<? extends Application> application);
    void stopApplicationStart(String application);

}
