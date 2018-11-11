package me.david.davidlib.startup.envirement;

import me.david.davidlib.application.Application;

public interface ConfiguriseEnvironmentChanger {

    void stopApplicationStart(Class<? extends Application> application);
    void stopApplicationStart(String application);

}
