package Server.Core;

import Server.Command.Server.JoinServerCommand;
import Server.Command.Server.ServerCommand;
import Server.Synchronize.ServerSynchronizer;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class BackEndServerConnection implements Runnable {

    private int id;
    private ServerSynchronizer serverSynchronizer;
    private ServerInfo requestingServer;
    public BackEndServerConnection(int myId, ServerSynchronizer serverSynchronizer, int requestingServerId) {
        if(myId == requestingServerId) {
            throw new IllegalArgumentException("Cannot connect to ourself.");
        }
        this.serverSynchronizer = serverSynchronizer;
        this.id = myId;
        this.requestingServer = serverSynchronizer.getServerInfo(requestingServerId);
    }

    @Override
    public void run() {
        Socket socket = null;
        try {
            socket = new Socket(requestingServer.getIpAddress(), requestingServer.getPort());
            socket.setKeepAlive(true);
            requestingServer.setTCPSocket(socket);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        JoinServerCommand sendingJoinCmd = new JoinServerCommand(new String[] {"join", "" + requestingServer.getId(), "" + 0},socket, serverSynchronizer, ServerCommand.Direction.Sending);
        sendingJoinCmd.execute();
        try {
            sendingJoinCmd.setOutputStream(new PrintStream(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        sendingJoinCmd.execute();
    }
}
