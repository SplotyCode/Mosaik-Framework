package me.david.davidlib.application;

import lombok.Getter;
import me.david.davidlib.datafactory.DataFactory;
import me.david.davidlib.startup.BootContext;
import me.david.davidlib.startup.envirement.ConfiguriseEnvironmentChanger;

import java.util.ArrayList;
import java.util.List;

public abstract class Application implements IApplication {

    @Getter private ShutdownManager localShutdownManager = new ShutdownManager();
    private static ShutdownManager globalShutdownManager = new ShutdownManager();

    @Getter private ApplicationState state = ApplicationState.NOT_FOUND;

    @Getter private DataFactory dataFactory = new DataFactory();
    @Getter private DataFactory config = new DataFactory();
    @Getter private List<Class<ApplicationType>> applicationTypes = new ArrayList<>();

    public abstract void start(BootContext context) throws Exception;
    public void configurise(ConfiguriseEnvironmentChanger environmentChanger, DataFactory config) throws Exception {}

    @Override
    public synchronized void setState(ApplicationState state) {
        this.state = state;
        notify();
    }

    public synchronized void awaitState(ApplicationState state) throws InterruptedException {
        while (this.state != state) {
            wait();
        }
    }

    @Override
    public Application getApplication() {
        return this;
    }

    @Override
    public ShutdownManager getGlobalShutdownManager() {
        return globalShutdownManager;
    }
}
