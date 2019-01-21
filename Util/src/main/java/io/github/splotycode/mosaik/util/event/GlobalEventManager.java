package io.github.splotycode.mosaik.util.event;

import lombok.Getter;

public class GlobalEventManager extends EventManager {

    private GlobalEventManager() {}

    @Getter private final static GlobalEventManager instance = new GlobalEventManager();

}
