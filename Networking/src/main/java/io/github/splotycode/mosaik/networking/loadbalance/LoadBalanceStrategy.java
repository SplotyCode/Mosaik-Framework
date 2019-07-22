package io.github.splotycode.mosaik.networking.loadbalance;

import io.netty.channel.Channel;

public interface LoadBalanceStrategy {

    void update(LoadBalancerService service);

    Channel nextServer();

}
