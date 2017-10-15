package Server.Command.Client;

import Server.BookKeeper;
import Server.Synchronize.ServerSynchronizer;

import java.net.Socket;

public class DeleteClientCommand extends ClientCommand {
    public DeleteClientCommand(String[] tokens, ServerSynchronizer synchronizer) {
        super(tokens, synchronizer);
    }

    @Override
    public String executeWhenInCS() {
        String name;
        String response;
        if (tokens.length != 2) {
            response = "Usage:\ndelete <name>\n";
        }
        name = tokens[1];
        response = store.delete(name);
        return response;
    }
}
