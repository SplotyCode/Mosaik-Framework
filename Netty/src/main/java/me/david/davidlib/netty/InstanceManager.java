package me.david.davidlib.netty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.*;

@AllArgsConstructor
public class InstanceManager<T extends INetServer> {

    @Getter @Setter private HashMap<Integer, T> servers = new HashMap<>();
    @Getter @Setter private int minPort, maxPort, maxInstances, startInstances;
    /* @Getter @Setter private Supplier<INetServer> create; */
    @Getter @Setter private ServerStarter<T> serverStarter;
    private int currentPort;
    private List<Integer> freePorts = new ArrayList<>();
    @Getter @Setter private List<Integer> portBlockList = new ArrayList<>();

    public interface ServerStarter<T extends INetServer> {

        T startServer(int port);

    }

    public void start() {
        if (startInstances == 0) startInstances = 1;
        currentPort = minPort;

        int i = 0;
        while (i != startInstances){
            int port = currentPort + 1;
            if (!portBlockList.contains(port)) {
                servers.put(port, serverStarter.startServer(port));
                i++;
            }
            currentPort++;
        }
    }

    private int getBestPort() {
        if (!freePorts.isEmpty()) return freePorts.stream().findFirst().get();

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

        /* Maby start a new server instance? */
        if (server.currentConnections() != 0 && servers.size() < maxInstances) {
            int port = getBestPort();
            server = serverStarter.startServer(port);
            servers.put(port, server);
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
