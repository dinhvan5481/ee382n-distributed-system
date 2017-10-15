
public class RequestServerCommand extends ServerCommand {

    public RequestServerCommand(String[] tokens, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, synchronizer, cmdDirection);
        cmd = ServerCommand.REQUEST_CMD;
    }

    @Override
    protected void executeReceiving() {
        //TODO: need command to parse the input
        ServerRequest request = new ServerRequest(sendingServerid, sendingServerClockValue, null);
        synchronizer.addRequestToList(request);
        ServerCommand ackServerCmd = new AckServerCommand(null, synchronizer, Direction.Sending);
        synchronizer.sendCommandTo(sendingServerid, ackServerCmd);
    }
}
