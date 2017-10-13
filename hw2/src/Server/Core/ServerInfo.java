package Server.Core;

import Server.Command.Server.JoinServerCommand;
import Server.Command.Server.ServerCommand;
import Server.Utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

public class ServerInfo {

    public static enum ServerState {
        UNKNOWN,
        JOIN,
        SYNC,
        READY,
        OFFLINE
    }

    private int id;
    private InetAddress ipAddress;
    private int port;
    private ServerState serverState;

    private Logger logger;

    public ServerInfo(int id, InetAddress ipAddress, int port) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.port = port;
        //TODO: may be wait for intialization process finished
        this.serverState = ServerState.UNKNOWN;
    }

    public int getId() {
        return id;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public int getPort() {
        return port;
    }

    public boolean isMe(int requestServerId) {
        return this.id == requestServerId;
    }

    public void setServerState(ServerState status) {
        this.serverState = status;
    }

    public ServerState getServerState() {
        return serverState;
    }
}
