package io.github.splotycode.mosaik.networking.statistics;

import io.github.splotycode.mosaik.networking.service.Service;

public interface StatisticService extends Service {

    ServiceStatistics statistics();

}
