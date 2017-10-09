package Server.Command.Client;
import Server.BookKeeper;
import Server.Synchronize.ServerSynchronizer;

import java.net.Socket;

public class BookSeatClientCommand extends ClientCommand {
    public BookSeatClientCommand(Socket clientSocket, BookKeeper store, ServerSynchronizer synchronizer) {
        super(clientSocket, store, synchronizer);
    }

    @Override
    public String executeClientCmd(String[] tokens) {
        String name;
        int seatNumber;
        if (tokens.length != 3) {
            return "Usage:\nbookSeat <name> <seatNum>\n";
        }
        name =  tokens[1];
        seatNumber = Integer.parseInt(tokens[2]);
        return store.bookSeat(name, seatNumber);
    }
}
