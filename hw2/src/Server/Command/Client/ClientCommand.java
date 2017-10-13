package Server.Command.Client;

import Server.BookKeeper;
import Server.Command.Command;
import Server.Synchronize.ServerSynchronizer;

import java.io.PrintStream;
import java.net.Socket;

public abstract class ClientCommand extends Command {

    protected BookKeeper store;
    protected ServerSynchronizer synchronizer;

    public ClientCommand(String[] tokens, BookKeeper store, ServerSynchronizer synchronizer) {
        super(tokens, synchronizer, Direction.Receiving);
        type = CommandType.Client;
    }

}
