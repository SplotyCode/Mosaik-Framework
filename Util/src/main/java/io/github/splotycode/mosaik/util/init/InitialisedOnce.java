package io.github.splotycode.mosaik.util.init;

import lombok.Getter;

public abstract class InitialisedOnce implements Initialisable {

    @Getter protected boolean initialised;

    public final void initalizeIfNotAlready() {
        if (!initialised) initalize();
    }

    public final void initalize() {
        if (initialised) throw new AlreadyInitailizedException();
        init();
        initialised = true;
    }

    protected abstract void init();

}
