
public class SearchClientCommand extends ClientCommand {

    public SearchClientCommand(String[] tokens, ServerSynchronizer synchronizer) {
        super(tokens, synchronizer);
    }

    @Override
    public String executeWhenInCS() {
        String name;
        String response;
        if (tokens.length != 2) {
            response = "Usage:\nsearch <name>\n";
        }
        name = tokens[1];
        response = store.search(name);
        return response;
    }
}
