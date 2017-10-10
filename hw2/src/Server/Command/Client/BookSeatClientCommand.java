package Server.Command.Client;
import Server.BookKeeper;
import Server.Synchronize.ServerSynchronizer;

import java.net.Socket;

public class BookSeatClientCommand extends ClientCommand {
    public BookSeatClientCommand(String[] tokens, Socket clientSocket, BookKeeper store, ServerSynchronizer synchronizer) {
        super(tokens, clientSocket, store, synchronizer);
    }

    @Override
    public void execute() {
        String name;
        String response;
        int seatNumber;
        if (tokens.length != 3) {
            response = "Usage:\nbookSeat <name> <seatNum>\n";
        }
        name =  tokens[1];
        seatNumber = Integer.parseInt(tokens[2]);
        response = store.bookSeat(name, seatNumber);
    }
}
