package Server.Core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;

public class ServerInfo {

    public static enum ServerState {
        JOIN,
        SYNC,
        READY,
        OFFLINE
    }

    private int id;
    private InetAddress ipAddress;
    private int port;
    private ServerState serverState;
    private Socket clientSocket;
    private BufferedReader inputStream;
    private PrintStream outputStream;


    public ServerInfo(int id, InetAddress ipAddress, int port) {
        this.id = id;
        this.ipAddress = ipAddress;
        this.port = port;
        //TODO: may be wait for intialization process finished
        this.serverState = ServerState.JOIN;
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

    public ServerState isOnline() {
        return serverState;
    }

    public void setServerState(ServerState status) {
        this.serverState = status;
    }

    public synchronized void sendMessage(int requestServerId, String message) {
        if(isMe(requestServerId)) {
            return;
        }
        sendTCPMessage(message);
    }

    public synchronized void setTCPSocket(Socket socket) throws IOException {
        this.clientSocket = socket;
        this.inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.outputStream = new PrintStream(clientSocket.getOutputStream());
    }

    private synchronized void sendTCPMessage(String message) {
        outputStream.println(message);
        outputStream.flush();
    }
}
