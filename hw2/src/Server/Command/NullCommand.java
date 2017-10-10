package Server.Command;

import java.net.Socket;

public class NullCommand extends Command {
    public NullCommand() {
        super(null, null);
    }

    @Override
    public void execute() {

    }
}
