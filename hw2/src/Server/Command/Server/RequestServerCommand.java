package Server.Command.Server;

import Server.Core.ServerRequest;
import Server.Synchronize.ServerSynchronizer;

public class RequestServerCommand extends ServerCommand {

    public RequestServerCommand(String[] tokens, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, synchronizer, cmdDirection);
        cmd = ServerCommand.REQUEST_CMD;
    }

    @Override
    protected String executeSending() {
//        long clockValue = synchronizer.getLogicalClock().tick();
//        ServerRequest request = new ServerRequest(serverId, clockValue);
//        synchronizer.setCurrentRequest(request);
//        String cmd = buildSendingCmd();
//        return cmd;
        return "";
    }

    @Override
    protected void executeReceiving() {
        synchronizer.getLogicalClock().tick(sendingServerClockValue);
        ServerRequest request = new ServerRequest(sendingServerid, sendingServerClockValue);
        synchronizer.addRequestToList(request);



    }
}
