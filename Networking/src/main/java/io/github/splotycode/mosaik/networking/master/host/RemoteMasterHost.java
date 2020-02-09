package io.github.splotycode.mosaik.networking.master.host;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.config.ConfigKey;
import io.github.splotycode.mosaik.networking.healthcheck.HealthCheck;
import io.github.splotycode.mosaik.networking.healthcheck.PingingHealthCheck;
import io.github.splotycode.mosaik.networking.host.AddressChangeListener;
import io.github.splotycode.mosaik.networking.host.HostProvider;
import io.github.splotycode.mosaik.networking.master.MasterService;
import io.github.splotycode.mosaik.networking.master.packets.DirectContactPacket;
import io.github.splotycode.mosaik.networking.master.packets.StartInstancePacket;
import io.github.splotycode.mosaik.networking.master.packets.StopInstancePacket;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.service.Service;
import io.github.splotycode.mosaik.networking.statistics.HostStatistics;
import io.github.splotycode.mosaik.networking.statistics.component.AbstractStatisticalHost;
import io.github.splotycode.mosaik.networking.statistics.remote.RemoteHostStatistics;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.github.splotycode.mosaik.util.listener.MultipleListenerHandler;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

public class RemoteMasterHost extends AbstractStatisticalHost implements MasterHost {

    public static final HostProvider PROVIDER = RemoteMasterHost::new;

    public static final ConfigKey<Long> HEALTH_THRESHOLD = new ConfigKey<>("master.host.health_threshold", long.class, 8 * 1000L);

    private CloudKit cloudKit;
    private MasterService masterService;

    private MultipleListenerHandler handler = new MultipleListenerHandler();

    private MosaikAddress address;

    private MasterHealthCheck healthCheck = new MasterHealthCheck();
    private PingingHealthCheck backupHealthCheck;

    @Setter @Getter private Channel channel;

    public RemoteMasterHost(CloudKit cloudKit, String address) {
        this.cloudKit = cloudKit;
        backupHealthCheck = new PingingHealthCheck(null, cloudKit.getConfig(HEALTH_THRESHOLD).intValue());
        tryInitialize();
        changeAddress(address);
    }

    public void changeAddress(String rawAddress) {
        MosaikAddress address = new MosaikAddress(rawAddress);
        handler.call(AddressChangeListener.class, (Consumer<AddressChangeListener>) listener -> listener.onChange(this.address, address));
        this.address = address;

        if (masterService != null) {
            backupHealthCheck.setAddress(address.asSocketAddress(masterService.getPort()));
        }
    }

    @Override
    public String toString() {
        return "Remote-" + address().asString();
    }

    @Override
    public void startNewInstance(String service) {
        channel.writeAndFlush(new StartInstancePacket(service));
    }

    @Override
    public void startNewInstance(Service service) {
        startNewInstance(service.displayName());
    }

    @Override
    public void stopService(String service, int port) {
        channel.writeAndFlush(new StopInstancePacket(service, port));
    }

    @Override
    public void sendPacket(SerializedPacket packet) {
        if (hasConnection()) {
            channel.writeAndFlush(packet).addListener(ChannelFutureListener.FIRE_EXCEPTION_ON_FAILURE);
        } else {
            masterService.sendMaster(new DirectContactPacket(packet, masterService, address()));
        }
    }

    protected boolean hasConnection() {
        return channel != null && channel.isOpen();
    }

    @Override
    public void initialize(MasterService masterService) {
        this.masterService = masterService;
        backupHealthCheck.setAddress(address.asSocketAddress(masterService.getPort()));
    }

    @Override
    public CloudKit cloudKit() {
        return cloudKit;
    }

    public long getLastUpdate() {
        return statistics().lastUpdate();
    }

    @Override
    public HostStatistics createStatistics() {
        return new RemoteHostStatistics(this, cloudKit());
    }

    private class MasterHealthCheck implements HealthCheck {

        @Override
        public boolean isOnline() {
            long lastUpdate = getLastUpdate();
            long delay = System.currentTimeMillis() - lastUpdate;
            boolean online = delay <= cloudKit.getConfig(HEALTH_THRESHOLD) + cloudKit.getConfig(MasterService.DAEMON_STATS_DELAY);
            if (lastUpdate == 0 || !online) {
                return backupHealthCheck.isOnline();
            }
            return true;
        }
    }

    @Override
    public HealthCheck healthCheck() {
        return healthCheck;
    }

    @Override
    public MosaikAddress address() {
        return address;
    }

    @Override
    public MultipleListenerHandler handler() {
        return handler;
    }
}
