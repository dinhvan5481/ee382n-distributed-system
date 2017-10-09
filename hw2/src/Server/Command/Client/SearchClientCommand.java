package Server.Command.Client;

import Server.BookKeeper;
import Server.Synchronize.ServerSynchronizer;

import java.net.Socket;

public class SearchClientCommand extends ClientCommand {

    public SearchClientCommand(Socket clientSocket, BookKeeper store, ServerSynchronizer synchronizer) {
        super(clientSocket, store, synchronizer);
    }

    @Override
    public String executeClientCmd(String[] tokens) {
        String name;
        if (tokens.length != 2) {
            return "Usage:\nsearch <name>\n";
        }
        name = tokens[1];
        return store.search(name);
    }
}
