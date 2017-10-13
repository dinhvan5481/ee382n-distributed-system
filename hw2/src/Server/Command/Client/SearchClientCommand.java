package Server.Command.Client;

import Server.BookKeeper;
import Server.Synchronize.ServerSynchronizer;

import java.net.Socket;

public class SearchClientCommand extends ClientCommand {

    public SearchClientCommand(String[] tokens, BookKeeper store, ServerSynchronizer synchronizer) {
        super(tokens, store, synchronizer);
    }

    @Override
    public void executeReceiving() {
        String name;
        String response;
        if (tokens.length != 2) {
            response = "Usage:\nsearch <name>\n";
        }
        name = tokens[1];
        response = store.search(name);
    }
}
