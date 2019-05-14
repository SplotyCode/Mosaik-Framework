package io.github.splotycode.mosaik.networking.statistics;

import io.github.splotycode.mosaik.networking.service.Service;

import java.util.Map;

public interface CostomStatisticService extends Service {

    Map<Integer, Integer> statistics();

}
