package Server.Command.Server;

import Server.Core.ServerRequest;
import Server.Synchronize.ServerSynchronizer;

public class RequestServerCommand extends ServerCommand {

    public RequestServerCommand(String[] tokens, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, synchronizer, cmdDirection);
        cmd = ServerCommand.REQUEST_CMD;
    }

    @Override
    protected void executeReceiving() {
        ServerRequest request = new ServerRequest(sendingServerid, sendingServerClockValue);
        synchronizer.addRequestToList(request);
        ServerCommand ackServerCmd = new AckServerCommand(null, synchronizer, Direction.Sending);
        synchronizer.sendCommandTo(sendingServerid, ackServerCmd);
    }
}
