package io.github.splotycode.mosaik.startup.application;

import io.github.splotycode.mosaik.startup.processbar.StartUpProcessHandler;
import io.github.splotycode.mosaik.runtime.application.Application;
import io.github.splotycode.mosaik.runtime.application.ApplicationState;
import io.github.splotycode.mosaik.runtime.application.ApplicationType;
import io.github.splotycode.mosaik.runtime.startup.BootException;
import io.github.splotycode.mosaik.util.AlmostBoolean;
import io.github.splotycode.mosaik.util.reflection.ClassCollector;

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
