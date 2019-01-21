package io.github.splotycode.mosaik.startup.application;

import io.github.splotycode.mosaik.startup.envirementchanger.ConfigureEnvironmentChangerImpl;
import lombok.Getter;
import io.github.splotycode.mosaik.runtime.LinkBase;
import io.github.splotycode.mosaik.runtime.application.Application;
import io.github.splotycode.mosaik.runtime.application.ApplicationHandle;
import io.github.splotycode.mosaik.runtime.application.ApplicationState;
import io.github.splotycode.mosaik.runtime.startup.ApplicationStartUpException;
import io.github.splotycode.mosaik.runtime.startup.BootContext;
import io.github.splotycode.mosaik.runtime.startup.BootException;

import java.lang.reflect.InvocationTargetException;

public class ApplicationHandleImpl implements ApplicationHandle {

    @Getter private Application application;

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
            throw new ApplicationStartUpException("Exception in " + application.getName() + "#start", e);
        }
        application.setState(ApplicationState.STARTED);
    }

}
