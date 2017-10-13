package Server.Command.Server;

import Server.Synchronize.ServerSynchronizer;

public class AckServerCommand extends ServerCommand {

    public AckServerCommand(String[] tokens, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, synchronizer, cmdDirection);
        cmd = ServerCommand.ACK_CMD;
    }

    @Override
    public void executeReceiving() {
        synchronizer.getLogicalClock().tick(sendingServerClockValue);
        //

    }
}
