
public class ReserveClientCommand extends ClientCommand {

    public ReserveClientCommand(String[] tokens, ServerSynchronizer synchronizer) {
        super(tokens, synchronizer);
    }

    @Override
    public String executeWhenInCS() {
        String name;
        String response;
        if (tokens.length != 2) {
           return "Usage:\nreserve <name>\n";
        }
        name = tokens[1];
        response = store.reserve(name);
        return response;
    }
}
