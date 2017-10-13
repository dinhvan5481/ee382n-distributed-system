package Server.Command.Server;

import Server.Command.Command;
import Server.Core.ServerInfo;
import Server.Synchronize.ServerSynchronizer;
import Server.Utils.Logger;

import java.util.LinkedList;
import java.util.List;

public abstract class ServerCommand extends Command {

    public enum Direction {
        Receiving,
        Sending
    }

    public static String ACK_JOIN_CMD = "ackjoin";
    public static String JOIN_CMD = "join";
    public static String REQUEST_CMD = "request";
    public static String ACK_CMD = "ack";
    public static String RELEASE_CMD = "release";


    protected String cmd;
    protected int serverId;
    protected List<String> additionalInfos;
    protected int sendingServerid;
    protected long sendingServerClockValue;
    protected ServerInfo receivedServerInfo;
    protected ServerSynchronizer synchronizer;
    protected Direction cmdDirection;

    protected ServerCommand(String[] tokens, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, synchronizer);
        additionalInfos = new LinkedList<>();
        if(cmdDirection == Direction.Receiving) {
            if(tokens != null && tokens.length >= 3) {
                this.cmd = tokens[0];
                this.sendingServerid = Integer.parseInt(tokens[1]);
                this.sendingServerClockValue = Long.parseLong(tokens[2]);
                for (int i = 3; i < tokens.length; i++) {
                    additionalInfos.add(tokens[i]);
                }
            }
        } else {
            if(tokens != null) {
                for (String token : tokens) {
                    additionalInfos.add(token);
                }
            }
        }
        this.serverId = synchronizer.getId();
        this.receivedServerInfo = synchronizer.getServerInfo(sendingServerid);
        //TODO: get addition info from the rest of tokens

        this.synchronizer = synchronizer;
        this.cmdDirection = cmdDirection;
    }

    public void execute() {
        String sendingCmd = null;
        if(cmdDirection == Direction.Sending) {
            sendingCmd = executeSending();
            logSendingCmd(sendingCmd);
        } else {
            executeReceiving();
        }
    }

    protected abstract String executeSending();
    protected abstract void executeReceiving();

    public String buildSendingCmd() {
        long clockValue = synchronizer.getLogicalClock().tick();
        StringBuilder sb = new StringBuilder(cmd + " " + serverId + " " + clockValue);
        if(additionalInfos.size() > 0) {
            for (String additionalInfo :
                    additionalInfos) {
                sb.append(" ").append(additionalInfo);
            }
        }
        return sb.toString();
    }

//    public void broadcast(ServerCommand command) {
//        for (ServerInfo serverInfo :
//                servers.values()) {
//            if (!serverInfo.isMe(id) && serverInfo.isOnline()) {
//                sendCommand(serverInfo, command);
//            }
//        }
//    }

    protected void logSendingCmd(String cmd) {
        //TODO: where is target server
        logger.log(Logger.LOG_LEVEL.DEBUG, "Server " + serverId + " send cmd: " + cmd + " to server " + sendingServerid);
    }
}
