package io.github.splotycode.mosaik.networking.healthcheck;

import io.github.splotycode.mosaik.util.task.TaskExecutor;
import io.github.splotycode.mosaik.util.task.types.RepeatableTask;
import lombok.Getter;
import lombok.Setter;

@Getter
public class ThresholdHealthCheck extends RepeatableTask implements HealthCheck {

    private long taskID;
    private final TaskExecutor executor;

    private HealthCheck backing;

    private boolean enabled;
    @Setter
    private int successThreshold, failThreshold;
    private boolean online = false;
    private boolean wasOnline = false;

    private int threshold;

    public ThresholdHealthCheck(HealthCheck backing, TaskExecutor executor, int interval,
                              int successThreshold, int failThreshold) {
        super("HealthCheck for " + backing.toString(), interval);
        this.backing = backing;
        this.executor = executor;
        this.successThreshold = successThreshold;
        this.failThreshold = failThreshold;
        setEnabled(true);
    }

    @Override
    public void run() {
        boolean hostOnline = backing.isOnline();
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
