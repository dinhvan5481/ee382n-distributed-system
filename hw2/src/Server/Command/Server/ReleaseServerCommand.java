package Server.Command.Server;

import Server.Synchronize.ServerSynchronizer;

import java.net.Socket;

public class ReleaseServerCommand extends ServerCommand {


    public ReleaseServerCommand(String[] tokens, Socket clientSocket, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, clientSocket, synchronizer, cmdDirection);
        cmd = "release";
    }

    public void setReleaseClockValue(long releaseClockValue) {
        additionalInfos.add("" + releaseClockValue);
    }

    @Override
    public void executeSending() {

    }

    @Override
    public void executeReceiving() {

    }
}
