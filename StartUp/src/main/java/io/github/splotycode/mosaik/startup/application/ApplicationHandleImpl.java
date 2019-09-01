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
import java.util.HashSet;
import java.util.Set;

public class ApplicationHandleImpl implements ApplicationHandle {

    @Getter private static Set<Class<? extends Application>> skippedClasses = new HashSet<>();
    @Getter private static Set<String> skippedNames = new HashSet<>();


    @Getter private Application application;

    public ApplicationHandleImpl(Application application) {
        this.application = application;
    }

    private static ConfigureEnvironmentChangerImpl environmentChanger = new ConfigureEnvironmentChangerImpl();

    @Override
    public void configurise() {
        if (skippedNames.contains(application.getName()) ||
            skippedClasses.contains(application.getClass())) {
            application.setState(ApplicationState.SKIPPED);
            return;
        }
        try {
            application.configurise(environmentChanger, application.getConfig());
        } catch (Throwable e) {
            throw new ApplicationStartUpException("Exception in " + application.getName() + "#configurise() method");
        } finally {
            application.setState(ApplicationState.CONFIGURISED);
        }
    }

    @Override
    public void start() {
        if (application.getState() == ApplicationState.SKIPPED) return;
        BootContext bootContext = LinkBase.getBootContext();
        application.setState(ApplicationState.STARTING);
        application.getApplicationTypes().forEach(type -> {
            try {
                application.getClass().getMethod("initType", BootContext.class, type).invoke(application, bootContext, null);
            } catch (NoSuchMethodException ignored) {

            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new BootException("Could not init Application Type " + type.getSimpleName() + " in " + application.getName(), ex);
            }
        });
        try {
            application.start(bootContext);
        } catch (Throwable e) {
            throw new ApplicationStartUpException("Exception in " + application.getName() + "#start", e);
        }
        application.setState(ApplicationState.STARTED);
    }

}
