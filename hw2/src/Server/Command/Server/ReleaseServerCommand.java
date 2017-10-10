package Server.Command.Server;

import Server.Core.ServerRequest;
import Server.Synchronize.ServerSynchronizer;

import java.net.Socket;
import java.util.LinkedList;

public class ReleaseServerCommand extends ServerCommand {


    public ReleaseServerCommand(String[] tokens, Socket clientSocket, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, clientSocket, synchronizer, cmdDirection);
        cmd = "release";
    }

    @Override
    public void executeSending() {
        long clockValue = synchronizer.getLogicalClock().tick();
        ServerRequest request = synchronizer.getCurrentRequest();
        LinkedList<String> additionInfo = new LinkedList<>();
        additionInfo.add("" + request   .getClockValue());
        String cmd = buildSendingCmd(clockValue, new String[] {"" + request.getClockValue()});
        sendTCPMessage(cmd);
    }

    @Override
    public void executeReceiving() {
        synchronizer.getLogicalClock().tick(receivedClockValue);

    }
}
