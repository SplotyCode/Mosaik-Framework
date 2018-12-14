package me.david.webapi.server.kaisa;

import me.david.davidlib.datafactory.DataKey;

import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.function.Supplier;

public final class KaisaConfig {

    public static final DataKey<Supplier<Queue<SocketChannel>>> WATING_QUEUE_SUPPLIER = new DataKey<>("wait_queue.supplier");
    public static final DataKey<Integer> WAITING_QUENE_MAX_CAPACITY = new DataKey<>("wait_queue.max_capacity");

}
