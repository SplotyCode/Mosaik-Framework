package io.github.splotycode.mosaik.networking.errorhandling;

import io.github.splotycode.mosaik.networking.cloudkit.CloudKitService;
import io.github.splotycode.mosaik.networking.component.tcp.TCPServer;
import io.github.splotycode.mosaik.networking.packet.PacketRegistry;
import io.github.splotycode.mosaik.networking.packet.serialized.SerializedPacket;
import io.github.splotycode.mosaik.networking.packet.system.DefaultPacketSystem;
import io.github.splotycode.mosaik.networking.service.ServiceStatus;
import io.github.splotycode.mosaik.networking.util.IpFilterHandler;
import io.github.splotycode.mosaik.networking.util.MosaikAddress;
import io.github.splotycode.mosaik.util.random.PatternGenerator;
import lombok.Getter;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Getter
public class ErrorService extends CloudKitService {

    private HashMap<String, ErrorEntry> localErrorEntries = new HashMap<>();
    private PatternGenerator idGenerator = PatternGenerator.getInstance().digits(4).groups(3);

    private AtomicInteger callBackID = new AtomicInteger();
    private ConcurrentHashMap<Integer, Consumer<ReportedError>> callBacks = new ConcurrentHashMap<>();

    private PacketRegistry<SerializedPacket> packetRegistry = new PacketRegistry<>(SerializedPacket.class);
    private ErrorServerHandler serverHandler = new ErrorServerHandler(this);

    private TCPServer errorServer = TCPServer.create().setDisplayName("Error Collector")
            .usePacketSystem(2, DefaultPacketSystem.createSerialized(packetRegistry))
            .childHandler(5, "packetHandler", serverHandler);

    @Override
    public void start() {
        errorServer.removeHandler(IpFilterHandler.class);
        errorServer.childHandler(1, "ipFiler", cloudKit.getIpFilter());
        errorServer.bind();
    }

    @Override
    public void stop() {
        errorServer.shutdown();
    }

    private String newId() {
        String id = null;
        while (id == null || !localErrorEntries.containsKey(id)) {
            id = idGenerator.build();
        }
        return id;
    }

    public ErrorEntry createReport(ErrorEntryID base) {
        ErrorEntryID id = new ErrorEntryID(cloudKit.getSelfHost().address(), newId());
        return new ErrorEntry(id, base);
    }

    public void resolveError(ErrorEntryID errorID, Consumer<ReportedError> callback) {
        int callbackID = callBackID.getAndIncrement();
        callBacks.put(callbackID, callback);

        MosaikAddress requester = cloudKit.selfAddress();
        MosaikAddress first = errorID.getHost();
        String nextID = first.asString();
        sendNext(first, new ResolveErrorPacket(new HashMap<>(), callbackID,requester, nextID));
    }

    public void sendNext(MosaikAddress next, ResolveErrorPacket packet) {
        if (next.isLocal(cloudKit)) {
            serverHandler.onTrace(packet);
        } else {
            //TODO connect to next and send packet
        }
    }

    @Override
    public ServiceStatus getStatus() {
        return ServiceStatus.RUNNING;
    }

    @Override
    public String statusMessage() {
        return "Locally holding " + localErrorEntries.size() + " error reports";
    }
}
