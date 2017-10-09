package Server.Command.Client;

import Server.BookKeeper;
import Server.Command.Command;
import Server.Synchronize.ServerSynchronizer;

import java.net.Socket;

public abstract class ClientCommand extends Command {

    protected BookKeeper store;
    protected ServerSynchronizer synchronizer;

    public ClientCommand(Socket clientSocket, BookKeeper store, ServerSynchronizer synchronizer) {
        super(clientSocket);
        this.store = store;
        this.synchronizer = synchronizer;
        type = CommandType.Client;
    }

    public abstract String executeClientCmd(String[] tokens);

    @Override
    public void executeServerCmd(String[] tokens) {
        return;
    }
}
