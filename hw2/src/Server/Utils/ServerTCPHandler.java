package Server.Utils;

import Server.BookKeeper;
import Server.Command.Client.*;
import Server.Command.Command;
import Server.Command.NullCommand;
import Server.Command.Server.*;
import Server.Core.ITCPConnection;
import Server.Core.ServerInfo;
import Server.Synchronize.ServerSynchronizer;

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
                Command result = parseServerInput(store, cmdFromClient);
                result.execute();
            }
        } catch (IOException e) {
            logger.log(Logger.LOG_LEVEL.INFO, "Connection with client ended.");
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.log(Logger.LOG_LEVEL.INFO, "Error while closing client socket");
                e.printStackTrace();
            }
        }
    }

    public void sendTCPMessage(String message) {
        outputStream.println(message);
        outputStream.flush();
    }

    public void sendCommand(ServerCommand serverCommand) {
        sendTCPMessage(serverCommand.buildSendingCmd());
    }

    private Command parseServerInput(BookKeeper store, String input) {
        String[] tokens = input.split(" ");
        String command = tokens[0].toLowerCase();
        String name;
        int seatNumber;

        switch (command) {
            case "reserve":
                return new ReserveClientCommand(tokens, store, synchronizer);

            case "bookseat":
                return new BookSeatClientCommand(tokens, store, synchronizer);

            case "delete":
                return new DeleteClientCommand(tokens, store, synchronizer);

            case "search":
                return new SearchClientCommand(tokens, store, synchronizer);

            case "join":
                int sendingServerId = Integer.parseInt(tokens[1]);
                this.neighborServer = synchronizer.getServerInfo(sendingServerId);
                synchronizer.setITCPConn(sendingServerId, this);
                return new JoinServerCommand(tokens, synchronizer, ServerCommand.Direction.Receiving);
            case "request":
                return new RequestServerCommand(tokens, synchronizer, ServerCommand.Direction.Receiving);
            case "ack":
                return new AckServerCommand(tokens, synchronizer, ServerCommand.Direction.Receiving);
            case "release":
                return new ReleaseServerCommand(tokens, synchronizer, ServerCommand.Direction.Receiving);


            default:
                return new NullCommand();
        }
    }
}
