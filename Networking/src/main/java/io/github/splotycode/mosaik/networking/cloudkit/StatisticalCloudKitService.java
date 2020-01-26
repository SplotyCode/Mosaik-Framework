package io.github.splotycode.mosaik.networking.cloudkit;

import io.github.splotycode.mosaik.networking.statistics.ServiceStatistics;
import io.github.splotycode.mosaik.networking.statistics.component.StatisticalService;

public abstract class StatisticalCloudKitService extends CloudKitService implements StatisticalService {

    protected volatile ServiceStatistics statistics;

    @Override
    public ServiceStatistics statistics() {
        if (statistics == null) {
            synchronized (this) {
                if (statistics == null) {
                    statistics = createStatistics();
                }
            }
        }
        return statistics;
    }

}
