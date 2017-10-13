package Server.Command.Server;

import Server.Command.Command;
import Server.Synchronize.ServerSynchronizer;
import Server.Utils.Logger;

public abstract class ServerCommand extends Command {
    public static String ACK_JOIN_CMD = "ackjoin";
    public static String JOIN_CMD = "join";
    public static String REQUEST_CMD = "request";
    public static String ACK_CMD = "ack";
    public static String RELEASE_CMD = "release";

    protected ServerCommand(String[] tokens, ServerSynchronizer synchronizer, Direction cmdDirection) {
        super(tokens, synchronizer, cmdDirection);
        this.type = CommandType.Server;
    }

    public String buildSendingCmd() {
        long clockValue = synchronizer.getLogicalClock().tick();
        StringBuilder sb = new StringBuilder(cmd + " " + serverId + " " + clockValue);
        if(additionalInfos.size() > 0) {
            for (String additionalInfo : additionalInfos) {
                sb.append(" ").append(additionalInfo);
            }
        }
        String cmd = sb.toString();
        return cmd;
    }
}
