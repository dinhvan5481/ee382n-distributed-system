package Server.Core;

import Server.Core.ServerInfo;
import Server.Server;
import Server.Synchronize.LogicalClock;

import java.util.HashMap;

public class SystemInfo {
    private HashMap<Integer, ServerInfo> servers;
    private LogicalClock logicalClock;

    public SystemInfo(LogicalClock clock) {
        servers = new HashMap<>();
        logicalClock = clock;
    }

    public void addServers(int id, ServerInfo serverInfo) {
        if(!servers.containsKey(id)) {
            servers.put(id, serverInfo);
        }
    }

    public LogicalClock getLogicalClock() {
        return logicalClock;
    }
}
