package me.david.davidlib.application;

import lombok.Getter;
import me.david.davidlib.datafactory.DataFactory;

import java.util.ArrayList;
import java.util.List;

public abstract class Application implements IApplication {

    @Getter private ShutdownManager localShutdownManager = new ShutdownManager();
    private static ShutdownManager globalShutdownManager = new ShutdownManager();

    @Getter private DataFactory dataFactory = new DataFactory();
    @Getter private DataFactory config = new DataFactory();
    @Getter private List<Class<ApplicationType>> applicationTypes = new ArrayList<>();

    public abstract void start(BootContext context);
    public void configurise(DataFactory config) {};

    @Override
    public Application getApplication() {
        return this;
    }

    @Override
    public ShutdownManager getGlobalShutdownManager() {
        return globalShutdownManager;
    }
}
