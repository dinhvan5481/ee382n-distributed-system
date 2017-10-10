package Server.Command.Client;

import Server.BookKeeper;
import Server.Command.Command;
import Server.Synchronize.ServerSynchronizer;

import java.io.PrintStream;
import java.net.Socket;

public abstract class ClientCommand extends Command {

    protected BookKeeper store;
    protected ServerSynchronizer synchronizer;

    public ClientCommand(String[] tokens, Socket clientSocket, BookKeeper store, ServerSynchronizer synchronizer) {
        super(tokens, clientSocket);
        this.store = store;
        this.synchronizer = synchronizer;
        type = CommandType.Client;
    }

}
