package Server.Command.Server;

import Server.Core.ServerRequest;
import Server.Synchronize.ServerSynchronizer;

import java.net.Socket;

public class RequestServerCommand extends ServerCommand {

    public RequestServerCommand(String[] tokens, Socket clientSocket, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, clientSocket, synchronizer, cmdDirection);
        cmd = "request";
    }

    @Override
    protected void executeSending() {
        long clockValue = synchronizer.getLogicalClock().tick();
        ServerRequest request = new ServerRequest(serverId, clockValue);
        synchronizer.setCurrentRequest(request);
        String cmd = buildSendingCmd(clockValue, null);
        sendTCPMessage(cmd);
    }

    @Override
    protected void executeReceiving() {
        synchronizer.getLogicalClock().tick(receivedClockValue);
        ServerRequest request = new ServerRequest(receivedServerId, receivedClockValue);
        synchronizer.addRequestToList(request);

        ServerCommand ackCommand = new AckServerCommand(new String[] {"ack", "" + serverId, "0" }, clientSocket, synchronizer, Direction.Sending);
        ackCommand.execute();
    }
}
