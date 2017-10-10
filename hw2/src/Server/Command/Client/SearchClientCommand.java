package Server.Command.Client;

import Server.BookKeeper;
import Server.Synchronize.ServerSynchronizer;

import java.net.Socket;

public class SearchClientCommand extends ClientCommand {

    public SearchClientCommand(String[] tokens, Socket clientSocket, BookKeeper store, ServerSynchronizer synchronizer) {
        super(tokens, clientSocket, store, synchronizer);
    }

    @Override
    public void execute() {
        String name;
        String response;
        if (tokens.length != 2) {
            response = "Usage:\nsearch <name>\n";
        }
        name = tokens[1];
        response = store.search(name);
    }
}
