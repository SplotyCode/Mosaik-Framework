package de.splotycode.davidlib.startup.application;

import de.splotycode.davidlib.startup.envirementchanger.ConfigureEnvironmentChangerImpl;
import me.david.davidlib.runtimeapi.LinkBase;
import me.david.davidlib.runtimeapi.application.Application;
import me.david.davidlib.runtimeapi.application.ApplicationHandle;
import me.david.davidlib.runtimeapi.application.ApplicationState;
import me.david.davidlib.runtimeapi.application.BootException;
import me.david.davidlib.runtimeapi.startup.ApplicationStartUpException;
import me.david.davidlib.runtimeapi.startup.BootContext;

import java.lang.reflect.InvocationTargetException;

public class ApplicationHandleImpl implements ApplicationHandle {

    private Application application;

    public ApplicationHandleImpl(Application application) {
        this.application = application;
    }

    private static ConfigureEnvironmentChangerImpl environmentChanger = new ConfigureEnvironmentChangerImpl();

    @Override
    public void configurise() {
        application.setState(ApplicationState.CONFIGURISED);
        try {
            application.configurise(environmentChanger, application.getConfig());
        } catch (Exception e) {
            throw new ApplicationStartUpException("Exception in " + application.getName() + "#configurise() method");
        }
    }

    @Override
    public void start() {
        BootContext bootContext = LinkBase.getBootContext();
        application.setState(ApplicationState.STARTING);
        application.getApplicationTypes().forEach(type -> {
            try {
                application.getClass().getMethod("initType", BootContext.class, type).invoke(application, bootContext, null);
            } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {
                throw new BootException("Could not init Application Type " + type.getSimpleName() + " in " + application.getName(), ex);
            }
        });
        try {
            application.start(bootContext);
        } catch (Exception e) {
            throw new ApplicationStartUpException("Exception in " + application.getName() + "#start");
        }
        application.setState(ApplicationState.STARTED);
    }

    @Override
    public Application getApplication() {
        return application;
    }
}
