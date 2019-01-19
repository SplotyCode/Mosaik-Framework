package de.splotycode.davidlib.startup.application;

import de.splotycode.davidlib.startup.processbar.StartUpProcessHandler;
import me.david.davidlib.runtime.application.Application;
import me.david.davidlib.runtime.application.ApplicationState;
import me.david.davidlib.runtime.application.ApplicationType;
import me.david.davidlib.runtime.startup.BootException;
import me.david.davidlib.util.AlmostBoolean;
import me.david.davidlib.util.reflection.ClassCollector;

public class ApplicationFinder {

    private static ClassCollector classCollector = ClassCollector.newInstance()
                                                    .setOnlyClasses(true)
                                                    .setNoDisableds(true)
                                                    .setAbstracation(AlmostBoolean.NO)
                                                    .setNeedAssignable(Application.class);

    private ApplicationManager manager;

    public ApplicationFinder(ApplicationManager manager) {
        this.manager = manager;
    }

    public void findAll() {
        StartUpProcessHandler.getInstance().newProcess("Finding Applications", classCollector.totalResults());
        for (Class<?> clazz : classCollector.collectAll()) {
            StartUpProcessHandler.getInstance().next();
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
