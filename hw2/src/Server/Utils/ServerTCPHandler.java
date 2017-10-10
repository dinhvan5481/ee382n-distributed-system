package Server.Utils;

import Server.BookKeeper;
import Server.Command.Client.*;
import Server.Command.Command;
import Server.Command.NullCommand;
import Server.Command.Server.*;
import Server.Synchronize.ServerSynchronizer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

public class ServerTCPHandler extends Thread {
    private Socket clientSocket;
    private BufferedReader inputStream;
    private PrintStream outputStream;
    private BookKeeper store;
    private ServerSynchronizer synchronizer;
    private Logger logger;

    public ServerTCPHandler(Socket clientSocket, BookKeeper store, ServerSynchronizer synchronizer) throws IOException {
        this.clientSocket = clientSocket;
        this.store = store;

        this.inputStream = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        this.outputStream = new PrintStream(clientSocket.getOutputStream());
        this.synchronizer = synchronizer;

        this.logger = new Logger(Logger.LOG_LEVEL.DEBUG);
    }

    public void run() {
        String cmdFromClient;
        try {
            while ((cmdFromClient = inputStream.readLine()) != null) {
                logger.log(Logger.LOG_LEVEL.INFO, String.format("Received from client: %s", cmdFromClient));
                Command result = parseServerInput(store, cmdFromClient);
                result.setOutputStream(outputStream);
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

    private void sendTCPMessage(String message) {
        outputStream.println(message);
        outputStream.flush();
    }

     private Command parseServerInput(BookKeeper store, String input) {
        String[] tokens = input.split(" ");
        String command = tokens[0].toLowerCase();
        String name;
        int seatNumber;

        switch (command) {
            case "reserve":
                return new ReserveClientCommand(tokens, clientSocket, store, synchronizer);

            case "bookseat":
                return new BookSeatClientCommand(tokens, clientSocket, store, synchronizer);

            case "delete":
                return new DeleteClientCommand(tokens, clientSocket, store, synchronizer);

            case "search":
                return new SearchClientCommand(tokens, clientSocket, store, synchronizer);

            case "join":
                return new JoinServerCommand(tokens, clientSocket, synchronizer, ServerCommand.Direction.Receiving);
            case "request":
                return new RequestServerCommand(tokens, clientSocket, synchronizer, ServerCommand.Direction.Receiving);
            case "ack":
                return new AckServerCommand(tokens, clientSocket, synchronizer, ServerCommand.Direction.Receiving);
            case "release":
                return new ReleaseServerCommand(tokens, clientSocket, synchronizer, ServerCommand.Direction.Receiving);


            default:
                return new NullCommand();
        }
    }
}
