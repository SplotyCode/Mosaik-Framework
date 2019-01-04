package me.david.davidlib.runtime.startup.envirement;

import me.david.davidlib.runtime.application.Application;

public interface ConfiguriseEnvironmentChanger {

    void stopApplicationStart(Class<? extends Application> application);
    void stopApplicationStart(String application);

}
