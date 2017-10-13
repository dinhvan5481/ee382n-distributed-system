package Server.Command.Server;

import Server.Synchronize.ServerSynchronizer;

import java.net.Socket;

public class NullServerCommand extends ServerCommand {
    public NullServerCommand() {
        super(null, null, Direction.Sending);
    }

    @Override
    public String executeSending() {
        return "null";
    }

    @Override
    public void executeReceiving() {

    }
}
