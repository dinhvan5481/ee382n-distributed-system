package Server.Command.Server;

import Server.Command.Command;
import Server.Core.ServerInfo;
import Server.Synchronize.ServerSynchronizer;

import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public abstract class ServerCommand extends Command {

    public static enum Direction {
        Receiving,
        Sending
    }

    protected String cmd;
    protected int serverId;
    protected List<String> additionalInfos;
    protected int receivedServerId;
    protected long receivedClockValue;
    protected ServerInfo receivedServerInfo;
    protected ServerSynchronizer synchronizer;
    protected Direction cmdDirection;

    protected ServerCommand(String[] tokens , Socket clientSocket, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, clientSocket);
        this.cmd = tokens[0];
        this.receivedServerId = Integer.parseInt(tokens[1]);
        this.receivedClockValue = Long.parseLong(tokens[2]);
        this.serverId = synchronizer.getId();
        this.receivedServerInfo = synchronizer.getServerInfo(receivedServerId);
        //TODO: get addition info from the rest of tokens
        additionalInfos = new LinkedList<>();
        this.synchronizer = synchronizer;
        this.cmdDirection = cmdDirection;
    }

    public void execute() {
        if(cmdDirection == Direction.Sending) {
            executeSending();
        } else {
            executeReceiving();
        }
    }

    public abstract void executeSending();
    public abstract void executeReceiving();

    public String buildSendingCmd(long clockValue, String[] additionInfo) {
        StringBuilder sb = new StringBuilder(cmd + " " + clockValue + " " + serverId);
        if(additionalInfos != null) {
            for (String additionalInfo :
                    additionalInfos) {
                sb.append(" " + additionalInfo);
            }
        }
        return sb.toString();
    }
}
