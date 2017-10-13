package Server.Command;

import Server.Command.Server.ServerCommand;
import Server.Core.ServerInfo;
import Server.Synchronize.ServerSynchronizer;
import Server.Utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public abstract class Command {
    public static enum CommandType {
        Client,
        Server
    }

    public enum Direction {
        Receiving,
        Sending
    }

    protected int serverId;
    protected CommandType type;
    protected String cmd;
    protected String[] tokens;
    protected List<String> additionalInfos;
    protected ServerSynchronizer synchronizer;
    protected int sendingServerid;
    protected long sendingServerClockValue;
    protected ServerInfo receivedServerInfo;
    protected ServerCommand.Direction cmdDirection;

    protected Logger logger;
    protected Command(String[] tokens, ServerSynchronizer synchronizer, ServerCommand.Direction cmdDirection) {
        this.serverId = synchronizer.getId();
        this.synchronizer = synchronizer;
        this.cmdDirection = cmdDirection;
        additionalInfos = new LinkedList<>();
        if(cmdDirection == ServerCommand.Direction.Receiving && getCommandType() == CommandType.Server) {
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
        if(getCommandType() == CommandType.Server) {
            this.receivedServerInfo = synchronizer.getServerInfo(sendingServerid);
        }
        logger = new Logger(Logger.LOG_LEVEL.DEBUG);
    }

    public CommandType getCommandType() {
        return type;
    }

    protected abstract void executeReceiving();

    public final void executeReceivingCmd() {
        if(cmdDirection == Direction.Receiving) {
            logger.log(Logger.LOG_LEVEL.DEBUG, synchronizer.toString() + ": receive " + tokens);
            synchronizer.getLogicalClock().tick(sendingServerClockValue);
            executeReceiving();
        }

    }
}
