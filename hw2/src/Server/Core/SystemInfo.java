package Server.Core;

import Server.Core.ServerInfo;
import Server.Server;
import Server.Synchronize.LogicalClock;
import Server.Synchronize.ServerSynchronizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.HashMap;

public class SystemInfo {


    private int myId;
    private HashMap<Integer, ServerInfo> servers;
    private LogicalClock logicalClock;

    private BufferedReader inputStream;
    private PrintStream outputStream;
    private Socket clientSocket;

    public SystemInfo(int id, LogicalClock clock) {
        servers = new HashMap<>();
        logicalClock = clock;
        myId = id;
    }

    public void addServers(int id, ServerInfo serverInfo) {
        if(!servers.containsKey(id)) {
            servers.put(id, serverInfo);
        }
    }

    public LogicalClock getLogicalClock() {
        return logicalClock;
    }

    public int getMyId() {
        return myId;
    }

}
