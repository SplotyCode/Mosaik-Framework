package io.github.splotycode.mosaik.startup.application;

import io.github.splotycode.mosaik.runtime.Runtime;
import io.github.splotycode.mosaik.runtime.application.*;
import io.github.splotycode.mosaik.startup.processbar.StartUpProcessHandler;
import io.github.splotycode.mosaik.util.StringUtil;
import io.github.splotycode.mosaik.util.collection.FilteredCollection;
import io.github.splotycode.mosaik.util.collection.MappedCollection;
import io.github.splotycode.mosaik.util.logger.Logger;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ApplicationManager implements IApplicationManager {
    private final Logger logger = Logger.getInstance(getClass());

    List<ApplicationHandleImpl> handles = new ArrayList<>();
    private Collection<ApplicationHandle> unmodifiableHandles = Collections.unmodifiableList(handles);
    @Getter Collection<Application> applications = Collections.unmodifiableCollection(new MappedCollection<>(handles, ApplicationHandleImpl::getApplication));

    @Getter private Collection<ApplicationHandle> loadedHandles = Collections.unmodifiableCollection(
            new FilteredCollection<>(handles,
                    handle -> handle.getApplication().getState() == ApplicationState.STARTED
            )
    );
    @Getter private Collection<Application> loadedApplications = Collections.unmodifiableCollection(
            new FilteredCollection<>(applications,
                    application -> application.getState() == ApplicationState.STARTED
            )
    );

    private ApplicationFinder applicationFinder;

    public ApplicationManager() {
        applicationFinder = new ApplicationFinder(this, Runtime.getRuntime().getGlobalClassPath());
    }

    public void start() {
        startApplications();
        logger.info("Started " + getLoadedApplicationsCount() + " Applications: " + StringUtil.join(getLoadedApplications(), IApplication::getName, ", "));
    }

    private void startApplications() {
        applicationFinder.findAll();
        StartUpProcessHandler.getInstance().newProcess("Configurise Applications", handles.size());
        handles.forEach(handle -> {
            StartUpProcessHandler.getInstance().next();
            handle.configurise();
        });
        StartUpProcessHandler.getInstance().newProcess("Starting Applications", handles.size());
        handles.forEach(handle -> {
            StartUpProcessHandler.getInstance().next();
            handle.start();
        });
        StartUpProcessHandler.getInstance().end();
    }

    @Override
    public ApplicationHandle getHandleByName(String name) {
        return handles.stream().filter(handle -> handle.getApplication().getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public int getLoadedApplicationsCount() {
        return loadedApplications.size();
    }

    @Override
    public Application getApplicationByName(String name) {
        return getHandleByName(name).getApplication();
    }

    @Override
    public ApplicationHandle getHandleByClass(Class<? extends Application> application) {
        return handles.stream().filter(handle -> handle.getApplication().getClass() == application).findFirst().orElse(null);
    }

    @Override
    public <A extends Application> A getApplicationByClass(Class<A> application) {
        return (A) getHandleByClass(application).getApplication();
    }

    @Override
    public Collection<ApplicationHandle> getHandles() {
        return unmodifiableHandles;
    }
}
