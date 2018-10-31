package me.david.davidlib.utils.init;

import lombok.Getter;

public abstract class InitialisedOnce implements Initialisable {

    @Getter protected boolean initialised;

    public final void initalize() {
        if (initialised) throw new AlreadyInitailizedException();
        initialised = true;
        init();
    }

    protected abstract void init();

}
