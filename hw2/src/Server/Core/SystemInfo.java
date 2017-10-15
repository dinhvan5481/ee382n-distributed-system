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
    private LogicalClock logicalClock;

    public SystemInfo(int id, LogicalClock clock) {
        logicalClock = clock;
        myId = id;
    }

    public LogicalClock getLogicalClock() {
        return logicalClock;
    }

    public int getMyId() {
        return myId;
    }

}
