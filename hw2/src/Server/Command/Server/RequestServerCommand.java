package Server.Command.Server;

import Server.Synchronize.ServerSynchronizer;

import java.net.Socket;

public class RequestServerCommand extends ServerCommand {

    public RequestServerCommand(String[] tokens, Socket clientSocket, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, clientSocket, synchronizer, cmdDirection);
        cmd = "request";
    }

    @Override
    public void executeSending() {

    }

    @Override
    public void executeReceiving() {

    }
}
