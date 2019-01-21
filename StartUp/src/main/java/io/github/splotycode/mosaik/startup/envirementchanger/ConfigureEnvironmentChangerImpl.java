package io.github.splotycode.mosaik.startup.envirementchanger;

import io.github.splotycode.mosaik.startup.exception.EnvironmentChangeExcpetion;
import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.application.Application;
import io.github.splotycode.mosaik.runtime.application.ApplicationState;
import io.github.splotycode.mosaik.runtime.startup.envirement.ConfiguriseEnvironmentChanger;

public class ConfigureEnvironmentChangerImpl implements ConfiguriseEnvironmentChanger {

    @Override
    public void stopApplicationStart(Class<? extends Application> application) {
        stopApplication(LinkBase.getApplicationManager().getApplicationByClass(application));
    }

    @Override
    public void stopApplicationStart(String name) {
        stopApplication(LinkBase.getApplicationManager().getApplicationByName(name));
    }

    private void stopApplication(Application application) {
        if (application.getState() != ApplicationState.FOUND && application.getState() != ApplicationState.CONFIGURISED) {
            throw new EnvironmentChangeExcpetion("Can not stop application in this state");
        }
        application.setState(ApplicationState.SKIPPED);
    }
}
