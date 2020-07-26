package io.github.splotycode.mosaik.startup.application;

import io.github.splotycode.mosaik.annotations.visibility.VisibilityLevel;
import io.github.splotycode.mosaik.startup.processbar.StartUpProcessHandler;
import io.github.splotycode.mosaik.runtime.application.Application;
import io.github.splotycode.mosaik.runtime.application.ApplicationState;
import io.github.splotycode.mosaik.runtime.application.ApplicationType;
import io.github.splotycode.mosaik.runtime.startup.BootException;
import io.github.splotycode.mosaik.util.AlmostBoolean;
import io.github.splotycode.mosaik.util.reflection.classpath.ClassPath;
import io.github.splotycode.mosaik.util.reflection.collector.ClassCollector;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@RequiredArgsConstructor
public class ApplicationFinder {

    private static ClassCollector classCollector = ClassCollector.newInstance()
                                                    .setOnlyClasses(true)
                                                    .setNoDisable(true)
                                                    .setVisibility(VisibilityLevel.NOT_INVISIBLE)
                                                    .setAbstracation(AlmostBoolean.NO)
                                                    .setNeedAssignable(Application.class);

    private final ApplicationManager manager;
    private final ClassPath classPath;

    public void findAll() {
        Collection<Class> classes = classCollector.collectAll(classPath);
        StartUpProcessHandler.getInstance().newProcess("Finding Applications", classes.size());
        for (Class<?> clazz : classes) {
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
            } catch (Throwable ex) {
                throw new BootException("Could not create Instance of " + clazz.getSimpleName(), ex);
            }
        }
    }

}
