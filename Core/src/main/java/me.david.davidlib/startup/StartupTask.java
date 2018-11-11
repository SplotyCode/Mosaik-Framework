package me.david.davidlib.startup;

import me.david.davidlib.startup.envirement.StartUpEnvironmentChanger;

public interface StartupTask {

    void execute(StartUpEnvironmentChanger environmentChanger) throws Exception;

}
