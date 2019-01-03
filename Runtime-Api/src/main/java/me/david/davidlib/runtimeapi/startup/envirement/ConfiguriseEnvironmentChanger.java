package me.david.davidlib.runtimeapi.startup.envirement;

import me.david.davidlib.util.core.application.Application;

public interface ConfiguriseEnvironmentChanger {

    void stopApplicationStart(Class<? extends Application> application);
    void stopApplicationStart(String application);

}
