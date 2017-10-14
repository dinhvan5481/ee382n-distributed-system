package Server.Command;

import java.net.Socket;

public class NullCommand extends Command {
    public NullCommand() {
        super(null, null, CommandType.Client, Direction.Receiving);
    }

    @Override
    protected void executeReceiving() {

    }
}
