package Server.Command.Server;

import Server.Core.ServerRequest;
import Server.Synchronize.ServerSynchronizer;

import java.util.LinkedList;

public class ReleaseServerCommand extends ServerCommand {


    public ReleaseServerCommand(String[] tokens, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, synchronizer, cmdDirection);
        cmd = ServerCommand.RELEASE_CMD;
    }

    @Override
    public String executeSending() {
        long clockValue = synchronizer.getLogicalClock().tick();
        ServerRequest request = synchronizer.getCurrentRequest();
        LinkedList<String> additionInfo = new LinkedList<>();
        additionInfo.add("" + request   .getClockValue());
        String cmd = buildSendingCmd();
        return cmd;
    }

    @Override
    public void executeReceiving() {
        synchronizer.getLogicalClock().tick(sendingServerClockValue);

    }
}
