package Server.Command.Server;

import java.net.Socket;

public class AckServerCommand extends ServerCommand {


    protected AckServerCommand(Socket clientSocket, int serverId, long clockValue) {
        super(clientSocket, serverId, clockValue);
        cmd = "ack";
    }

    @Override
    public void executeServerCmd(String[] tokens) {

    }
}
