package de.splotycode.davidlib.startup.application;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.david.davidlib.application.Application;
import me.david.davidlib.application.ApplicationHandle;
import me.david.davidlib.application.ApplicationState;
import me.david.davidlib.application.BootException;
import me.david.davidlib.link.LinkBase;
import me.david.davidlib.startup.ApplicationStartUpException;
import me.david.davidlib.startup.BootContext;

import java.lang.reflect.InvocationTargetException;

@AllArgsConstructor
public class ApplicationHandleImpl implements ApplicationHandle {

    @Getter private Application application;

    @Override
    public void configurise() {
        application.setState(ApplicationState.CONFIGURISED);
        try {
            application.configurise(application.getConfig());
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
}
