package me.david.davidlib.netty;

import lombok.Getter;
import lombok.Setter;
import me.david.davidlib.netty.server.INetServer;
import me.david.davidlib.util.listener.ListenerHandler;

import java.util.*;

public class InstanceManager<T extends INetServer> extends ListenerHandler<InstanceListener> {

    @Getter @Setter private HashMap<Integer, T> servers = new HashMap<>();
    @Getter @Setter private int minPort, maxPort, maxInstances, startInstances;
    /* @Getter @Setter private Supplier<INetServer> create; */
    @Getter @Setter private ServerStarter<T> serverStarter;
    private int currentPort;
    private List<Integer> preferredPorts = new ArrayList<>();
    @Getter @Setter private List<Integer> portBlockList = new ArrayList<>();

    public interface ServerStarter<T extends INetServer> {

        T startServer(int port);

    }

    public void addPreferredPort(int port) {
        preferredPorts.add(port);
    }

    public InstanceManager(int minPort, int maxPort, int maxInstances, int startInstances, ServerStarter<T> serverStarter) {
        this.minPort = minPort;
        this.maxPort = maxPort;
        this.maxInstances = maxInstances;
        this.startInstances = startInstances;
        this.serverStarter = serverStarter;
    }

    public void start() {
        if (startInstances == 0) startInstances = 1;
        currentPort = minPort;

        int i = 0;
        while (i != startInstances){
            if (!portBlockList.contains(currentPort)) {
                servers.put(currentPort, serverStarter.startServer(currentPort));
                i++;
                call(listener -> listener.startServer(currentPort, true));
            }
            currentPort++;
        }
        call(listener -> listener.startInstances(startInstances));
    }

    private int getBestPort() {
        Optional<Integer> optional = preferredPorts.stream().findFirst();
        if (optional.isPresent()) return optional.get();

        while (true) {
            currentPort++;
            if (!portBlockList.contains(currentPort)) return currentPort;
        }
    }

    public INetServer getServer(NetSession session) {
        /* Get Server with the most free connections */
        Optional<T> optional = servers.values().stream().max(Comparator.comparingInt(t -> t.maxConnections() - t.currentConnections()));
        if (!optional.isPresent()) return null;

        T server = optional.get();

        /* Maybe start a new server instance? */
        if (server.currentConnections() != 0 && servers.size() < maxInstances) {
            int port = getBestPort();
            server = serverStarter.startServer(port);
            servers.put(port, server);
            call(listener -> listener.startServer(port, false));
        /* All servers are full */
        } else if (server.maxConnections() >= server.currentConnections()) {
            return null;
        }
        return server;
    }

    public void shutdown() {
        servers.forEach((integer, server) -> server.shutDown());
    }

}
