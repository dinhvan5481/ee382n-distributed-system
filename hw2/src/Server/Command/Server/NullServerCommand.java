package Server.Command.Server;

import Server.Synchronize.ServerSynchronizer;

import java.net.Socket;

public class NullServerCommand extends ServerCommand {
    protected NullServerCommand() {
        super(null, null, null, Direction.Sending);
    }

    @Override
    public void executeSending() {

    }

    @Override
    public void executeReceiving() {

    }
}
