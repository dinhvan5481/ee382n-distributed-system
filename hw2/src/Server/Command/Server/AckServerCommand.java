package Server.Command.Server;

import Server.Synchronize.ServerSynchronizer;

import java.net.Socket;

public class AckServerCommand extends ServerCommand {

    public AckServerCommand(String[] tokens, Socket clientSocket, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, clientSocket, synchronizer, cmdDirection);
        cmd = "ack";
    }

    @Override
    public void executeSending() {
        long clockValue = synchronizer.getLogicalClock().tick();
        String cmd = buildSendingCmd(clockValue, null);
        sendTCPMessage(cmd);
    }

    @Override
    public void executeReceiving() {
        synchronizer.getLogicalClock().tick(receivedClockValue);
        //

    }
}
