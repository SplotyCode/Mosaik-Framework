package de.splotycode.davidlib.startup.application;

import de.splotycode.davidlib.startup.processbar.StartUpProcessHandler;
import lombok.Getter;
import me.david.davidlib.application.Application;
import me.david.davidlib.application.ApplicationHandle;
import me.david.davidlib.application.ApplicationState;
import me.david.davidlib.link.IApplicationManager;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ApplicationManager implements IApplicationManager {

    List<ApplicationHandleImpl> handles = new ArrayList<>();
    private ApplicationFinder applicationFinder = new ApplicationFinder(this);

    public void startUp() {
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
    }

    @Override
    public ApplicationHandle getHandleByName(String name) {
        return handles.stream().filter(handle -> handle.getApplication().getName().equals(name)).findFirst().orElse(null);
    }

    @Override
    public Collection<Application> getLoadedApplications() {
        Collection<Application> applications = getApplications();
        for (Application application : new ArrayList<>(applications)) {
            if (application.getState() != ApplicationState.STARTED) {
                applications.remove(application);
            }
        }
        return applications;
    }

    @Override
    public Collection<ApplicationHandle> getLoadedHandles() {
        Collection<ApplicationHandle> handles = new ArrayList<>(getHandles());
        for (ApplicationHandle handle : new ArrayList<>(handles)) {
            if (handle.getApplication().getState() != ApplicationState.STARTED) {
                handles.remove(handle);
            }
        }
        return handles;
    }

    @Override
    public int getLoadedApplicationsCount() {
        int loaded = 0;
        for (ApplicationHandle handle : handles) {
            if (handle.getApplication().getState() == ApplicationState.STARTED) {
                loaded++;
            }
        }
        return loaded;
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
    public Application getApplicationByClass(Class<? extends Application> application) {
        return getHandleByClass(application).getApplication();
    }

    @Override
    public Collection<Application> getApplications() {
        List<Application> applications = new ArrayList<>();
        handles.forEach(handle -> applications.add(handle.getApplication()));
        return applications;
    }

    @Override
    public Collection<ApplicationHandle> getHandles() {
        return new ArrayList<>(handles);
    }
}
