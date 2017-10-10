package Server.Command.Server;

import Server.Synchronize.ServerSynchronizer;

import java.net.Socket;

public class AckServerCommand extends ServerCommand {

    public AckServerCommand(String[] tokens, Socket clientSocket, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, clientSocket, synchronizer, cmdDirection);
        cmd = "ack";
    }

    @Override
    public void execute() {

    }

    @Override
    public void executeSending() {

    }

    @Override
    public void executeReceiving() {

    }
}
