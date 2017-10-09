package Server.Command.Client;

import Server.BookKeeper;
import Server.Synchronize.ServerSynchronizer;

import java.net.Socket;

public class DeleteClientCommand extends ClientCommand {
    public DeleteClientCommand(Socket clientSocket, BookKeeper store, ServerSynchronizer synchronizer) {
        super(clientSocket, store, synchronizer);
    }

    @Override
    public String executeClientCmd(String[] tokens) {
        String name;
        if (tokens.length != 2) {
            return "Usage:\ndelete <name>\n";
        }
        name = tokens[1];
        return store.delete(name);
    }
}
