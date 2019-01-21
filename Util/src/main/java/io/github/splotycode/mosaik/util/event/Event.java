package io.github.splotycode.mosaik.util.event;

public class Event {

    public void callGlobal() {
        call(GlobalEventManager.getInstance());
    }

    public void call(final EventManager eventManager) {
        eventManager.call(this);
    }
}
