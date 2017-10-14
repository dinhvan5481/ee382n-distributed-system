package Server.Core;

import Server.Command.Command;
import Server.Command.Server.*;
import Server.Synchronize.ServerSynchronizer;
import Server.Utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class BackEndServerConnection implements ITCPConnection {
    private ServerSynchronizer serverSynchronizer;
    private ServerInfo neighborServer;

    private Socket socket;
    private BufferedReader inputStream;
    private PrintStream outputStream;

    private Logger logger;

    public BackEndServerConnection(ServerSynchronizer serverSynchronizer, ServerInfo neighborServer) throws IOException {
        if(serverSynchronizer.getId() == neighborServer.getId()) {
            throw new IllegalArgumentException("Cannot connect to ourself.");
        }
        this.serverSynchronizer = serverSynchronizer;
        this.neighborServer = neighborServer;

        this.socket = new Socket(this.neighborServer.getIpAddress(), this.neighborServer.getPort());
        //this.socket.setKeepAlive(true);
        this.inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.outputStream = new PrintStream(socket.getOutputStream());

        this.logger = new Logger(Logger.LOG_LEVEL.DEBUG);
    }

    public BackEndServerConnection(ServerSynchronizer serverSynchronizer, ServerInfo neighborServer, Socket socket) throws IOException {
        if(serverSynchronizer.getId() == neighborServer.getId()) {
            throw new IllegalArgumentException("Cannot connect to ourself.");
        }
        this.serverSynchronizer = serverSynchronizer;
        this.neighborServer = neighborServer;

        this.socket = socket;
        this.socket.setKeepAlive(true);
        this.inputStream = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.outputStream = new PrintStream(socket.getOutputStream());

        this.logger = new Logger(Logger.LOG_LEVEL.DEBUG);
    }

    @Override
    public void run() {
        String cmdFromNeighborServer;
        try {
            while ((cmdFromNeighborServer = inputStream.readLine()) != null) {
                logger.log(Logger.LOG_LEVEL.INFO, String.format( "Server %d -  Received from neighbor server: %s",
                        serverSynchronizer.getId(), cmdFromNeighborServer));

                Command result = Command.parseCommand(cmdFromNeighborServer, serverSynchronizer);
                if(result.getCommandType() == Command.CommandType.Null) {
                    continue;
                }
                result.executeReceivingCmd();
            }

        } catch (IOException e) {

            e.printStackTrace();
        } finally {
            logger.log(Logger.LOG_LEVEL.INFO, serverSynchronizer.toString() + ": Server " + neighborServer.getId() + " went offline");
            neighborServer.setServerState(ServerInfo.ServerState.OFFLINE);
        }

    }

    public void sendTCPMessage(String message) {
        logSendingCmd(message);
        outputStream.println(message);
        outputStream.flush();
    }

    public void sendCommand(ServerCommand serverCommand) {
        sendTCPMessage(serverCommand.buildSendingCmd());
    }

    protected void logSendingCmd(String cmd) {
        logger.log(Logger.LOG_LEVEL.DEBUG, serverSynchronizer.toString() + " send message: " + cmd + " to server " + neighborServer.getId());
    }
}
