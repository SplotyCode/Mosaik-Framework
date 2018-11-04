package me.david.davidlib.application;

public abstract class Application implements IApplication {

    public abstract void start(BootContext context);

    @Override
    public Application getApplication() {
        return this;
    }

}
