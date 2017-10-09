package Server.Command.Server;

import Server.Command.Command;

import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public abstract class ServerCommand extends Command {
    protected String cmd;
    protected int serverId;
    protected long clockValue;
    protected List<String> additionalInfos;

    protected ServerCommand(Socket clientSocket, int serverId, long clockValue) {
        super(clientSocket);
        this.cmd = "base";
        this.serverId = serverId;
        this.clockValue = clockValue;
        additionalInfos = new LinkedList<>();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(cmd + " " + clockValue + " " + serverId);
        if(additionalInfos != null) {
            for (String additionalInfo :
                    additionalInfos) {
                sb.append(" " + additionalInfo);
            }
        }
        return sb.toString();
    }

    @Override
    public String executeClientCmd(String[] tokens) {
        return "";
    }

    public abstract void executeServerCmd(String[] tokens);}
