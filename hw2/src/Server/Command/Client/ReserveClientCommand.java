package Server.Command.Client;

import Server.BookKeeper;
import Server.Synchronize.ServerSynchronizer;

import java.net.Socket;

public class ReserveClientCommand extends ClientCommand {

    public ReserveClientCommand(Socket clientSocket, BookKeeper store, ServerSynchronizer synchronizer) {
        super(clientSocket, store, synchronizer);
    }

    @Override
    public String executeClientCmd(String[] tokens) {
        String name;
        if (tokens.length != 2) {
            return "Usage:\nreserve <name>\n";
        }
        name = tokens[1];
        return store.reserve(name);
    }
}
