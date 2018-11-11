package de.splotycode.davidlib.startup.envirementchanger;

import de.splotycode.davidlib.startup.exception.EnvironmentChangeExcpetion;
import me.david.davidlib.application.Application;
import me.david.davidlib.application.ApplicationState;
import me.david.davidlib.link.LinkBase;
import me.david.davidlib.startup.envirement.ConfiguriseEnvironmentChanger;

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
        application.setState(ApplicationState.SKIPED);
    }
}
