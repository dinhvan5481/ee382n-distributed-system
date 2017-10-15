import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ServerTCPHandler implements ITCPConnection {
    private Socket clientSocket;
    private BufferedReader inputStream;
    private PrintStream outputStream;
    private BookKeeper store;
    private ServerSynchronizer synchronizer;
    private ServerInfo neighborServer;
    private Logger logger;

    public ServerTCPHandler(Socket clientSocket, BookKeeper store, ServerSynchronizer synchronizer) throws IOException {
        this.clientSocket = clientSocket;
        this.store = store;

        this.inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.outputStream = new PrintStream(clientSocket.getOutputStream());
        this.synchronizer = synchronizer;

        this.logger = new Logger(Logger.LOG_LEVEL.DEBUG);
    }

    @Override
    public void run() {
        String cmdFromClient;
        try {
            while ((cmdFromClient = inputStream.readLine()) != null) {
                logger.log(Logger.LOG_LEVEL.INFO, String.format("Server %d -  Received from client: %s", synchronizer.getId(), cmdFromClient));
                Command result = Command.parseCommand(cmdFromClient, synchronizer);
                if(result.getCommandType() == Command.CommandType.Null) {
                    continue;
                }
                if(result.getCommandType() == Command.CommandType.Server) {
                    int sendingServerId = result.getSendingServerid();
                    this.neighborServer = synchronizer.getServerInfo(sendingServerId);
                    synchronizer.setITCPConn(sendingServerId, this);
                } else {
                    ((ClientCommand)result).setTcpConnection(this);
                }
                result.executeReceivingCmd();
            }
        } catch (IOException e) {
            logger.log(Logger.LOG_LEVEL.INFO, "Connection with client ended.");
        } finally {
            if(neighborServer != null) {
                synchronizer.setNeighborServerState(neighborServer.getId(), ServerInfo.ServerState.OFFLINE);
            }
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.log(Logger.LOG_LEVEL.INFO, "Error while closing client socket");
                e.printStackTrace();
            }
        }
    }

    public void sendTCPMessage(String message) {
        logSendingCmd(message);
        outputStream.println(message);
        outputStream.flush();
    }

    public void sendCommand(ServerCommand serverCommand) {
        synchronizer.getLogicalClock().tick();
        sendTCPMessage(serverCommand.buildSendingCmd());
    }

    protected void logSendingCmd(String cmd) {
        if(neighborServer != null) {
            logger.log(Logger.LOG_LEVEL.DEBUG, synchronizer.toString() + " send message: " + cmd + " to server " + neighborServer.getId());
        }

    }

}
