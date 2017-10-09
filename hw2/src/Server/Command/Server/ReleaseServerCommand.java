package Server.Command.Server;

import java.net.Socket;

public class ReleaseServerCommand extends ServerCommand {


    protected ReleaseServerCommand(Socket clientSocket, int serverId, long clockValue) {
        super(clientSocket, serverId, clockValue);
        cmd = "release";
    }

    public void setReleaseClockValue(long releaseClockValue) {
        additionalInfos.add("" + releaseClockValue);
    }

    @Override
    public void executeServerCmd(String[] tokens) {

    }
}
