package de.splotycode.davidlib.startup.application;

import de.splotycode.davidlib.startup.processbar.StartUpProcessHandler;
import me.david.davidlib.runtime.application.Application;
import me.david.davidlib.runtime.application.ApplicationState;
import me.david.davidlib.runtime.application.ApplicationType;
import me.david.davidlib.runtime.startup.BootException;
import me.david.davidlib.util.reflection.ClassFinderHelper;
import me.david.davidlib.util.reflection.ReflectionUtil;

import java.util.Collection;

public class ApplicationFinder {

    private ApplicationManager manager;

    public ApplicationFinder(ApplicationManager manager) {
        this.manager = manager;
    }

    public void findAll() {
        Collection<Class<?>> classes = ClassFinderHelper.getUserClasses();
        StartUpProcessHandler.getInstance().newProcess("Finding Applications", classes.size());
        for (Class<?> clazz : classes) {
            StartUpProcessHandler.getInstance().next();
            if (ReflectionUtil.validClass(clazz, Application.class, true, true)) {
                try {
                    Application application = (Application) clazz.newInstance();
                    application.setState(ApplicationState.FOUND);
                    manager.handles.add(new ApplicationHandleImpl(application));
                    for (Class<?> type : clazz.getInterfaces()) {
                        if (ApplicationType.class.isAssignableFrom(type)) {
                            application.getApplicationTypes().add((Class<ApplicationType>) type);
                        }
                    }
                } catch (InstantiationException | IllegalAccessException ex) {
                    throw new BootException("Could not create Instance of " + clazz.getSimpleName(), ex);
                }
            }
        }
    }

}
