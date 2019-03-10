package io.github.splotycode.mosaik.webapi.server.kaisa;

import io.github.splotycode.mosaik.util.datafactory.DataKey;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class KaisaConfig {

    public static final DataKey<Supplier<Queue<SocketChannel>>> WAITING_QUEUE_SUPPLIER = new DataKey<>("wait_queue.supplier");
    public static final DataKey<Integer> WAITING_QUENE_MAX_CAPACITY = new DataKey<>("wait_queue.max_capacity");

    public static final DataKey<Supplier<ExecutorService>> CONNECTION_EXECUTOR = new DataKey<>("connection_executor.executor");
    public static final DataKey<Integer> CONNECTION_EXECUTOR_PARALLEL = new DataKey<>("connection_executor.parallel");

    public static final DataKey<Supplier<ExecutorService>> DOWNLOAD_EXECUTOR = new DataKey<>("download.executor");
    public static final DataKey<Integer> DOWNLOAD_EXECUTOR_PARALLEL = new DataKey<>("download.parallel");

}
