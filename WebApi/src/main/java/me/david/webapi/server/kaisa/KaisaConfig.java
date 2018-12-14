package me.david.webapi.server.kaisa;

import me.david.davidlib.datafactory.DataKey;

import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

public final class KaisaConfig {

    public static final DataKey<Supplier<Queue<SocketChannel>>> WAITING_QUEUE_SUPPLIER = new DataKey<>("wait_queue.supplier");
    public static final DataKey<Integer> WAITING_QUENE_MAX_CAPACITY = new DataKey<>("wait_queue.max_capacity");

    public static final DataKey<Supplier<ExecutorService>> CONNECTION_EXECUTOR = new DataKey<>("connection_executor.executor");
    public static final DataKey<Integer> CONNECTION_EXECUTOR_PARALLEL = new DataKey<>("connection_executor.parallel");

}
