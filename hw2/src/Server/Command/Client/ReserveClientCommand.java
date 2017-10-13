package Server.Command.Client;

import Server.BookKeeper;
import Server.Synchronize.ServerSynchronizer;

import java.net.Socket;

public class ReserveClientCommand extends ClientCommand {

    public ReserveClientCommand(String[] tokens, BookKeeper store, ServerSynchronizer synchronizer) {
        super(tokens, store, synchronizer);
    }

    @Override
    public void execute() {
        String name;
        String response;
        if (tokens.length != 2) {
            response = "Usage:\nreserve <name>\n";
        }
        name = tokens[1];
        response = store.reserve(name);
    }
}
