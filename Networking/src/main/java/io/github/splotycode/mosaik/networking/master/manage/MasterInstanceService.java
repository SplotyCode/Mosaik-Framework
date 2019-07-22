package io.github.splotycode.mosaik.networking.master.manage;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKit;
import io.github.splotycode.mosaik.networking.component.INetworkProcess;
import io.github.splotycode.mosaik.networking.master.MasterChangeListener;
import io.github.splotycode.mosaik.networking.master.MasterService;
import io.github.splotycode.mosaik.networking.statistics.ServiceStatistics;
import io.github.splotycode.mosaik.networking.statistics.StatisticService;
import io.github.splotycode.mosaik.networking.statistics.StatisticalHost;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.github.splotycode.mosaik.networking.util.PortSupplier;
import io.github.splotycode.mosaik.util.task.types.RepeatableTask;
import lombok.Getter;

import java.util.Collection;
import java.util.Map;

@Getter
public abstract class MasterInstanceService<C extends INetworkProcess> implements StatisticService, MasterChangeListener {

    protected CloudKit kit;
    protected MasterService master;

    protected PortSupplier portSupplier;
    protected InstanceInvigilator invigilator;
    protected LocalInstanceManager<C> localManager;
    protected long taskID = -1;
    protected long delay;

    public MasterInstanceService(CloudKit kit, String prefix) {
        this(kit.getConfigProvider().getConfigValue(prefix + ".update", long.class, 16 * 1000L),
                kit, PortSupplier.fromConfig(kit.getConfigProvider(), prefix),
                (MasterService) kit.getServiceByName("Master Sync"),
                null, null);
        invigilator = InstanceInvigilator.fromConfig(this, prefix);
        localManager = new LocalInstanceManager<>(this);
    }

    public MasterInstanceService(long delay, CloudKit kit, PortSupplier portSupplier, MasterService master,
                                 InstanceInvigilator invigilator, LocalInstanceManager<C> localManager) {
        this.kit = kit;
        this.delay = delay;
        this.portSupplier = portSupplier;
        this.master = master;
        this.invigilator = invigilator;
        this.localManager = localManager;
    }

    @Override
    public void onChange(boolean own, MosaikAddress current, MosaikAddress last) {
        if (own) {
            if (taskID != -1) throw new IllegalStateException("Task was already running?");
            taskID = kit.getLocalTaskExecutor().runTask(new RepeatableTask(displayName(), () -> invigilator.updateComponents(), delay));
        } else {
            kit.getLocalTaskExecutor().stopTask(taskID);
            taskID = -1;
        }
    }

    @Override
    public void start() {
        onChange(master.isSelf(), null, null);
    }

    @Override
    public void stop() {
        if (taskID != -1) {
            kit.getLocalTaskExecutor().stopTask(taskID);
            taskID = -1;
        }
    }

    protected abstract C createComponent(int port);

    public Collection<C> getLocalInstances() {
        return getLocalInstanceMap().values();
    }

    public Map<Integer, C> getLocalInstanceMap() {
        return localManager.instances;
    }

    @Override
    public ServiceStatistics statistics() {
        ServiceStatistics statistics = new ServiceStatistics((StatisticalHost) kit.getSelfHost(), displayName());
        getLocalInstances().forEach(statistics::addComponent);
        return statistics;
    }

    @Override
    public String statusMessage() {
        return "Controlling " + localManager.getInstances().size() + " instances";
    }
}
