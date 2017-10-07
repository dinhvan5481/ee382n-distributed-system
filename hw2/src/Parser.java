
/**
 * Created by dinhvan5481 on 8/29/17.
 */
public class Parser {
    public static String parseServerInput(BookKeeper store, String input) {
        String[] tokens = input.split(" ");
        String command = tokens[0];
        String name;
        int seatNumber;

        switch (command) {
            case "reserve":
                if (tokens.length != 2) {
                    return "Usage:\nreserve <name>\n";
                }
                name = tokens[1];
                return store.reserve(name);

            case "bookSeat":
                if (tokens.length != 3) {
                    return "Usage:\nbookSeat <name> <seatNum>\n";
                }
                name =  tokens[1];
                seatNumber = Integer.parseInt(tokens[2]);
                return store.bookSeat(name, seatNumber);

            case "delete":
                if (tokens.length != 2) {
                    return "Usage:\ndelete <name>\n";
                }
                name = tokens[1];
                return store.delete(name);


            case "search":
                if (tokens.length != 2) {
                    return "Usage:\nsearch <name>\n";
                }
                name = tokens[1];
                return store.search(name);

            default:
                return String.format("Unknown command %s\n", command);
        }
    }
}
