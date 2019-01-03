package de.splotycode.davidlib.startup.application;

import de.splotycode.davidlib.startup.processbar.StartUpProcessHandler;
import lombok.AllArgsConstructor;
import me.david.davidlib.util.core.application.Application;
import me.david.davidlib.util.core.application.ApplicationState;
import me.david.davidlib.util.core.application.ApplicationType;
import me.david.davidlib.util.core.application.BootException;
import me.david.davidlib.util.reflection.ClassFinderHelper;
import me.david.davidlib.util.reflection.ReflectionUtil;

import java.util.Collection;

@AllArgsConstructor
public class ApplicationFinder {

    private ApplicationManager manager;

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
