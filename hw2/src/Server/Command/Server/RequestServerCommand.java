package Server.Command.Server;

import java.net.Socket;

public class RequestServerCommand extends ServerCommand {

    public RequestServerCommand(Socket clientSocket, int serverId, long clockValue) {
        super(clientSocket, serverId, clockValue);
        cmd = "request";
    }

    @Override
    public void executeServerCmd(String[] tokens) {

    }
}
