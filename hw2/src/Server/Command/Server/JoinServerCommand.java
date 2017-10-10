package Server.Command.Server;

import Server.Command.Command;
import Server.Command.NullCommand;
import Server.Core.ServerInfo;
import Server.Synchronize.ServerSynchronizer;
import Server.Utils.Logger;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;

public class JoinServerCommand extends ServerCommand {
    private int endpointServerId;

    public JoinServerCommand(String[] tokens, Socket clientSocket, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, clientSocket, synchronizer, cmdDirection);
        cmd = "join";
    }

    @Override
    public void executeSending() {
        long clockValue = synchronizer.getLogicalClock().tick();
        String cmd = buildSendingCmd(clockValue, null);
        sendTCPMessage(cmd);

        String response;
        try {
            while ((response = inputStream.readLine()) != null) {
                ServerCommand result = parseServerInput(response);
                result.setOutputStream(outputStream);
                result.execute();
            }
        } catch (IOException e) {
            synchronizer.getServerInfo(endpointServerId).setOnlineStatus(ServerInfo.ServerState.OFFLINE);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @Override
    public void executeReceiving() {
        receivedServerInfo.setOnlineStatus(ServerInfo.ServerState.JOIN);
    }

    private ServerCommand parseServerInput(String input) {
        String[] tokens = input.split(" ");
        String command = tokens[0].toLowerCase();
        switch (command) {
            case "join":
                return new JoinServerCommand(tokens, clientSocket, synchronizer, Direction.Receiving);
            case "request":
                return new RequestServerCommand(tokens, clientSocket, synchronizer, Direction.Receiving);
            case "ack":
                return new AckServerCommand(tokens, clientSocket, synchronizer, Direction.Receiving);
            case "release":
                return new ReleaseServerCommand(tokens, clientSocket, synchronizer, Direction.Receiving);

            default:
                return new NullServerCommand();

        }
    }

}
