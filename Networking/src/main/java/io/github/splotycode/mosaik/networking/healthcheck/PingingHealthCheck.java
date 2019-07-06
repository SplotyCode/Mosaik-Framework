package io.github.splotycode.mosaik.networking.healthcheck;

import io.github.splotycode.mosaik.util.NetworkUtil;
import io.github.splotycode.mosaik.util.task.TaskExecutor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.net.SocketAddress;

@Getter
@AllArgsConstructor
public class PingingHealthCheck implements HealthCheck {

    public static ThresholdHealthCheck createThresHold(SocketAddress address, int timeout, TaskExecutor executor, int interval,
                                                     int successThreshold, int failThreshold) {
        return new ThresholdHealthCheck(new PingingHealthCheck(address, timeout), executor, interval, successThreshold, failThreshold);
    }

    private final SocketAddress address;
    @Setter private int timeout;

    @Override
    public boolean isOnline() {
        return NetworkUtil.isOnline(address, timeout);
    }
}
