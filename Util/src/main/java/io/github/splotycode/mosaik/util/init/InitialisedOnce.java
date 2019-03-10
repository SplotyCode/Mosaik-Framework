package io.github.splotycode.mosaik.util.init;

import lombok.Getter;

public abstract class InitialisedOnce implements Initialisable {

    @Getter protected boolean initialised;

    public final void initalizeIfNotAlready() {
        if (!initialised) initalize();
    }

    public final void initalize() {
        if (initialised) fail();
        init();
        initialised = true;
    }

    public void fail() {
        throw new AlreadyInitailizedException();
    }

    protected abstract void init();

}
