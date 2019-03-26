package io.github.splotycode.mosaik.netty.healthcheck;

import io.github.splotycode.mosaik.util.NetworkUtil;
import io.github.splotycode.mosaik.util.task.TaskExecutor;
import io.github.splotycode.mosaik.util.task.types.RepeatableTask;
import lombok.Getter;
import lombok.Setter;

import java.net.SocketAddress;

@Getter
public class PingingHealthCheck extends RepeatableTask implements HealthCheck {

    private final SocketAddress address;
    private final TaskExecutor executor;
    @Setter private int timeout;
    private boolean enabled;
    @Setter private int successThreshold, failThreshold;
    private long taskID;
    private boolean online = false;
    private boolean wasOnline = false;

    private int threshold;

    public PingingHealthCheck(SocketAddress address, TaskExecutor executor, int interval, int timeout,
                              int successThreshold, int failThreshold) {
        super("HealthCheck of" + address.toString(), interval);
        this.address = address;
        this.timeout = timeout;
        this.executor = executor;
        this.successThreshold = successThreshold;
        this.failThreshold = failThreshold;
        setEnabled(true);
    }

    @Override
    public void run() {
        boolean hostOnline = NetworkUtil.isOnline(address, timeout);
        if (online != hostOnline) {
            threshold = 0;
        } else {
            threshold++;
            if (hostOnline && threshold >= successThreshold) {
                threshold = 0;
                online = true;
            } else if (!hostOnline && threshold >= failThreshold) {
                threshold = 0;
                online = false;
            }
        }
        wasOnline = hostOnline;
    }

    public void setEnabled(boolean enabled) {
        if (this.enabled != enabled) {
            if (enabled) {
                taskID = executor.runTask(this);
            } else {
                executor.stopTask(taskID);
            }
        }
        this.enabled = enabled;
    }
}
