package io.github.splotycode.mosaik.networking.util;

import io.github.splotycode.mosaik.valuetransformer.TransformerManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.ipfilter.AbstractRemoteAddressFilter;
import io.netty.handler.ipfilter.IpFilterRule;
import io.netty.handler.ipfilter.IpFilterRuleType;

import java.net.InetSocketAddress;
import java.util.Collection;

public abstract class IpFilterHandler extends AbstractRemoteAddressFilter<InetSocketAddress> implements IpFilterRule {

    @Override
    public boolean matches(InetSocketAddress address) {
        return grantedAddresses().contains(TransformerManager.getInstance().transform(address, String.class));
    }

    @Override
    public IpFilterRuleType ruleType() {
        return IpFilterRuleType.ACCEPT;
    }

    @Override
    protected boolean accept(ChannelHandlerContext ctx, InetSocketAddress address) throws Exception {
        return matches(address);
    }

    protected abstract Collection<String> grantedAddresses();

}
