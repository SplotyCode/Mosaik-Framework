package me.david.davidlib.runtimeapi.application;

import me.david.davidlib.util.core.application.Application;
import me.david.davidlib.util.core.application.ApplicationHandle;

import java.util.Collection;

public interface IApplicationManager {

    Collection<ApplicationHandle> getHandles();
    Collection<Application> getApplications();

    ApplicationHandle getHandleByName(String name);
    Application getApplicationByName(String name);
    ApplicationHandle getHandleByClass(Class<? extends Application> application);
    <A extends Application> A getApplicationByClass(Class<A> application);

    int getLoadedApplicationsCount();
    Collection<ApplicationHandle> getLoadedHandles();
    Collection<Application> getLoadedApplications();

}
