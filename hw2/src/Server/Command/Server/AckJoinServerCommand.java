package Server.Command.Server;

import Server.Synchronize.ServerSynchronizer;

import java.net.Socket;

public class AckJoinServerCommand extends ServerCommand {
    protected AckJoinServerCommand(String[] tokens, Socket clientSocket, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, clientSocket, synchronizer, cmdDirection);
        cmd = "ackjoin";
    }

    @Override
    public void executeSending() {
        long clockValue = synchronizer.getLogicalClock().tick();
        String cmd = buildSendingCmd(clockValue, null);
        sendTCPMessage(cmd);
    }

    @Override
    public void executeReceiving() {
        //Start sync process here
        synchronizer.getLogicalClock().tick(receivedClockValue);

    }
}
