package me.david.davidlib.netty.client;

import lombok.Getter;
import me.david.davidlib.util.event.EventManager;
import me.david.davidlib.util.event.EventPriority;
import me.david.davidlib.util.event.EventTarget;
import me.david.davidlib.util.event.GlobalEventManager;

public class ClientReconnectHandler {

    @Getter protected final int START_TIME, MAX_TIME, TIME_MULTIPLIER;

    @Getter private int currentTime;

    @Getter private final INetClient client;

    public ClientReconnectHandler(final INetClient client) {
        this(GlobalEventManager.getInstance(), client);
    }

    public ClientReconnectHandler(final EventManager eventManager, final INetClient client) {
        eventManager.register(this);
        START_TIME = 4 * 1000;
        MAX_TIME = 32 * 1000;
        TIME_MULTIPLIER = 2;
        this.client = client;
        currentTime = START_TIME;
    }

    public ClientReconnectHandler(final INetClient client, EventManager eventManager, int START_TIME, int MAX_TIME, int TIME_MULTIPLIER) {
        eventManager.register(this);
        this.START_TIME = START_TIME;
        this.MAX_TIME = MAX_TIME;
        this.TIME_MULTIPLIER = TIME_MULTIPLIER;
        this.client = client;
        currentTime = START_TIME;
    }

    @EventTarget(priority = EventPriority.LOW)
    public void onReconnect(final ClientReconnectEvent event) {
        if (event.getClient() == client) {
            event.setCanceled(false);
            if (currentTime * TIME_MULTIPLIER <= MAX_TIME) {
                currentTime *= TIME_MULTIPLIER;
            }
            event.setSleepTime(currentTime);
        }
    }

    @EventTarget
    public void onConnect(final ClientConnectedEvent event) {
        if (event.getClient() == client) {
            currentTime = START_TIME;
        }
    }
}
