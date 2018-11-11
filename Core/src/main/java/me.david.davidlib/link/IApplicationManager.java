package me.david.davidlib.link;

import me.david.davidlib.application.Application;
import me.david.davidlib.application.ApplicationHandle;

import java.util.Collection;

public interface IApplicationManager {

    Collection<ApplicationHandle> getHandles();
    Collection<Application> getApplications();

    Application getApplicationByName(String name);
    ApplicationHandle getHandleByName(String name);

    int getLoadedApplicationsCount();
    Collection<ApplicationHandle> getLoadedHandles();
    Collection<Application> getLoadedApplications();

}
