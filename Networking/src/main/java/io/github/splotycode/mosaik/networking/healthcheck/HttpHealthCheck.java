package io.github.splotycode.mosaik.networking.healthcheck;

import io.github.splotycode.mosaik.util.task.TaskExecutor;
import lombok.Data;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

@Data
public class HttpHealthCheck implements HealthCheck {

    public static ThresholdHealthCheck createThresHold(URL url, int timeout, TaskExecutor executor, int interval,
                                                       int successThreshold, int failThreshold) {
        return new ThresholdHealthCheck(new HttpHealthCheck(url, timeout), executor, interval, successThreshold, failThreshold);
    }

    private final URL url;
    private final int timeout;

    @Override
    public boolean isOnline() {
        try {
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(timeout);
            return connection.getResponseCode() / 100 == 2;
        } catch (IOException e) {
            return false;
        }
    }
}
