package Server.Utils;

import Server.BookKeeper;
import Server.Command.Client.BookSeatClientCommand;
import Server.Command.Client.DeleteClientCommand;
import Server.Command.Client.ReserveClientCommand;
import Server.Command.Client.SearchClientCommand;
import Server.Command.Command;
import Server.Command.NullCommand;
import Server.Synchronize.ServerSynchronizer;

import java.io.*;
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
//                result.execute();
            }
        } catch (IOException e) {
            logger.log(Logger.LOG_LEVEL.INFO, "Connection with client ended.");
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                logger.log(Logger.LOG_LEVEL.INFO, "Error while closing client socket");
                e.printStackTrace();
                return;
            }
        }
    }

    private void sendTCPMessage(String message) {
        outputStream.println(message);
        outputStream.flush();
    }

     public Command parseServerInput(BookKeeper store, String input) {
        String[] tokens = input.split(" ");
        String command = tokens[0].toLowerCase();
        String name;
        int seatNumber;

        switch (command) {
            case "reserve":
                return new ReserveClientCommand(clientSocket, store, synchronizer);

            case "bookseat":
                return new BookSeatClientCommand(clientSocket, store, synchronizer);


            case "delete":
                return new DeleteClientCommand(clientSocket, store, synchronizer);

            case "search":
                return new SearchClientCommand(clientSocket, store, synchronizer);

            case "join":


            default:
                return new NullCommand(clientSocket);
        }
    }
}
